package com.example.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.*;
import com.example.entity.vo.request.AddCommentVO;
import com.example.entity.vo.request.TopicCreateVO;
import com.example.entity.vo.request.TopicUpdateVO;
import com.example.entity.vo.response.CommentVO;
import com.example.entity.vo.response.TopicDetailVO;
import com.example.entity.vo.response.TopicPreviewVO;
import com.example.entity.vo.response.TopicTopVO;
import com.example.mapper.*;
import com.example.service.AccountPrivacyService;
import com.example.service.TopicService;
import com.example.utils.CacheUtils;
import com.example.utils.Const;
import com.example.utils.FlowUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import net.sf.jsqlparser.statement.select.Top;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by 刘千山 on 2023/12/13/013-22:06
 */
@Service
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic> implements TopicService {
    @Resource
    TopicTypeMapper topicTypeMapper;

    @Resource
    FlowUtils flowUtils;

    @Resource
    AccountMapper accountMapper;

    @Resource
    AccountDetailsMapper accountDetailsMapper;

    @Resource
    AccountPrivacyMapper accountPrivacyMapper;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    CacheUtils cacheUtils;

    @Resource
    TopicCommentMapper topicCommentMapper;

    @PostConstruct
    private void initTypes() {
        types = this.listTypes()
                .stream()
                .map(TopicType::getId)
                .collect(Collectors.toSet());
    }

    private Set<Integer> types = null;

    @Override
    public List<TopicType> listTypes() {
        return topicTypeMapper.selectList(null);
    }

    @Override
    public String createTopic(int uid, TopicCreateVO vo) {
        if (!textLimitCheck(vo.getContent(), 20000)) return "文章字数太多！发文失败";
        if (!types.contains(vo.getType())) return "文章类型非法";
        String key = Const.FORUM_TOPIC_CREATE_COUNTER + uid;
        if (!flowUtils.limitPeriodCounterCheck(key, 3, 2)) return "发文次数频繁，请稍后再试";
        Topic topic = new Topic();
        BeanUtils.copyProperties(vo, topic);
        topic.setContent(vo.getContent().toJSONString());
        topic.setUid(uid);
        topic.setTime(new Date());
        if (this.save(topic)) {
            cacheUtils.deleteCachePartner(Const.FORUM_TOPIC_PREVIEW_CACHE + "*");
            return null;
        } else {
            return "内部错误，请联系管理员!";
        }
    }

    @Override
    public String updateTopic(int uid, TopicUpdateVO vo) {
        if (!textLimitCheck(vo.getContent(), 20000)) return "文章字数太多！发文失败";
        if (!types.contains(vo.getType())) return "文章类型非法";
        baseMapper.update(null, Wrappers.<Topic>update()
                .eq("uid", uid)
                .eq("id", vo.getId())
                .set("title", vo.getTitle())
                .set("content", vo.getContent().toString())
                .set("type", vo.getType()));
        return null;
    }

    @Override
    public String createComment(AddCommentVO vo, int uid) {
        if (!textLimitCheck(JSONObject.parseObject(vo.getContent()), 200))
            return "发布字数太多！";
        String key = Const.FORUM_TOPIC_COMMENT_COUNTER + uid;
        if (!flowUtils.limitPeriodCounterCheck(key, 2, 10))
            return "发表评论频繁！";
        TopicComment topicComment = new TopicComment();
        topicComment.setUid(uid);
        BeanUtils.copyProperties(vo, topicComment);
        topicComment.setTime(new Date());
        topicCommentMapper.insert(topicComment);
        return null;
    }

    @Override
    public List<CommentVO> comments(int tid, int pageNumber) {
        Page<TopicComment> page = Page.of(pageNumber, 10);
        topicCommentMapper.selectPage(page, Wrappers.<TopicComment>query().eq("tid", tid));
        return page.getRecords().stream().map(dto -> {
            CommentVO commentVO = new CommentVO();
            BeanUtils.copyProperties(dto, commentVO);
            if (dto.getQuote() > 0) {
                JSONObject object = JSONObject.parseObject(
                        topicCommentMapper.selectOne(Wrappers.<TopicComment>query().eq("id",dto.getId())
                                .orderByAsc("time")).getContent()
                );
                StringBuilder builder = new StringBuilder();
                this.shortContent(object.getJSONArray("ops"),builder,ignore->{});
                commentVO.setQuote(builder.toString());
            }
            CommentVO.User user = new CommentVO.User();
            this.fillUserDetailsByPrivacy(user,dto.getUid());
            commentVO.setUser(user);
            return commentVO;
        }).toList();
    }


    @Override
    public List<TopicPreviewVO> listTopicByPage(int pageNum, int type) {
        String key = Const.FORUM_TOPIC_PREVIEW_CACHE + pageNum + ":" + type;
        List<TopicPreviewVO> list = cacheUtils.takeListFromCache(key, TopicPreviewVO.class);
        if (list != null) return list;
        Page<Topic> page = Page.of(pageNum, 10);
        if (type == 0) {
            baseMapper.selectPage(page, Wrappers.<Topic>query().orderByDesc("time"));
        } else {
            baseMapper.selectPage(page, Wrappers.<Topic>query().eq("type", type).orderByDesc("time"));
        }
        List<Topic> topics = page.getRecords();
        if (topics.isEmpty()) return null;
        list = topics.stream().map(this::resolveToPreview).toList();
        cacheUtils.saveListToCache(key, list, 60);
        return list;
    }

    @Override
    public List<TopicTopVO> listTopTopics() {
        List<Topic> topics = baseMapper.selectList(Wrappers.<Topic>query()
                .select("id", "title", "time")
                .eq("top", 1));
        return topics.stream().map(topic -> {
            TopicTopVO vo = new TopicTopVO();
            BeanUtils.copyProperties(topic, vo);
            return vo;
        }).toList();
    }

    @Override
    public List<TopicPreviewVO> listTopicsCollects(int uid) {
        return baseMapper.collectTopics(uid).stream().map(topic -> {
            TopicPreviewVO vo = new TopicPreviewVO();
            BeanUtils.copyProperties(topic, vo);
            return vo;
        }).toList();
    }

    private TopicPreviewVO resolveToPreview(Topic topic) {
        TopicPreviewVO vo = new TopicPreviewVO();
        BeanUtils.copyProperties(accountMapper.selectById(topic.getUid()), vo);
        BeanUtils.copyProperties(topic, vo);
        vo.setLike(baseMapper.interactCount(topic.getId(), "like"));
        vo.setCollect(baseMapper.interactCount(topic.getId(), "collect"));
        List<String> images = new ArrayList<>();
        StringBuilder previewText = new StringBuilder();
        JSONArray ops = JSONObject.parseObject(topic.getContent()).getJSONArray("ops");
        this.shortContent(ops,previewText,obj->images.add(obj.toString()));
        vo.setText(previewText.length() > 300 ? previewText.substring(0, 300) : previewText.toString());
        vo.setImages(images);
        return vo;
    }

    private void shortContent(JSONArray ops, StringBuilder previewText, Consumer<Object> imageHandler){
        for (Object op : ops) {
            Object insert = JSONObject.from(op).get("insert");
            if (insert instanceof String text) {
                if (previewText.length() >= 300) continue;
                previewText.append(text);
            } else if (insert instanceof Map<?, ?> map) {
                Optional.ofNullable(map.get("image")).ifPresent(imageHandler);
            }
        }
    }


    @Override
    public TopicDetailVO getTopic(int tid, int uid) {
        TopicDetailVO topicDetailVO = new TopicDetailVO();
        Topic topic = baseMapper.selectById(tid);
        BeanUtils.copyProperties(topic, topicDetailVO);
        TopicDetailVO.Interact interact = new TopicDetailVO.Interact(
                hasInteract(tid, uid, "like"),
                hasInteract(tid, uid, "collect")
        );
        topicDetailVO.setInteract(interact);
        TopicDetailVO.User user = new TopicDetailVO.User();
        topicDetailVO.setUser(this.fillUserDetailsByPrivacy(user, topic.getUid()));
        topicDetailVO.setComments(topicCommentMapper.selectCount(Wrappers.<TopicComment>query().eq("tid",tid)));
        return topicDetailVO;
    }

    @Override
    public void interact(Interact interact, boolean state) {
        String type = interact.getType();
        synchronized (type.intern()) {
            stringRedisTemplate.opsForHash().put(type, interact.toKey(), Boolean.toString(state));
            this.saveInteractSchedule(type);
        }
    }


    private boolean hasInteract(int tid, int uid, String type) {
        String key = tid + ":" + uid;
        if (stringRedisTemplate.opsForHash().hasKey(type, key)) {
            return Boolean.parseBoolean(stringRedisTemplate.opsForHash().entries(type).get(key).toString());
        }
        return baseMapper.userInteractCount(tid, uid, type) > 0;
    }

    private final Map<String, Boolean> state = new HashMap<>();
    ScheduledExecutorService service = Executors.newScheduledThreadPool(2);

    private void saveInteractSchedule(String type) {
        if (!state.getOrDefault(type, false)) {
            state.put(type, true);
            service.schedule(() -> {
                try {
                    this.saveInteract(type);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                state.put(type, false);
            }, 3, TimeUnit.SECONDS);
        }
    }

    private void saveInteract(String type) {
        synchronized (type.intern()) {
            List<Interact> check = new ArrayList<>();
            List<Interact> uncheck = new ArrayList<>();
            stringRedisTemplate.opsForHash().entries(type).forEach((k, v) -> {
                if (Boolean.parseBoolean(v.toString())) {
                    check.add(Interact.parseInteract(k.toString(), type));
                } else {
                    uncheck.add(Interact.parseInteract(k.toString(), type));
                }
            });
            if (!check.isEmpty()) {
                baseMapper.addInteract(check, type);
            }
            if (!uncheck.isEmpty()) {
                baseMapper.deleteInteract(uncheck, type);
            }
            stringRedisTemplate.delete(type);
        }
    }

    private <T> T fillUserDetailsByPrivacy(T t, int uid) {
        Account account = accountMapper.selectById(uid);
        AccountDetails accountDetails = accountDetailsMapper.selectById(uid);
        AccountPrivacy privacy = accountPrivacyMapper.selectById(uid);
        String[] hiddenFields = privacy.hiddenFields();
        BeanUtils.copyProperties(account, t, hiddenFields);
        BeanUtils.copyProperties(accountDetails, t, hiddenFields);
        return t;
    }

    private boolean textLimitCheck(JSONObject jsonObject, int max) {
        if (jsonObject == null) return false;
        long length = 0;
        for (Object op : jsonObject.getJSONArray("ops")) {
            length += JSONObject.from(op).getString("insert").length();
            if (length > 20000) return false;
        }
        return true;
    }
}
