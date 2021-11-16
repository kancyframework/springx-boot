package com.kancy.dingtalk;

import com.kancy.dingtalk.request.ActionCardDingTalkRequest;
import com.kancy.dingtalk.request.FeedCardDingTalkRequest;
import org.junit.Before;
import org.junit.Test;

/**
 * DingTalkClientTests
 *
 * @author huangchengkang
 * @date 2021/11/16 15:42
 */
public class DingTalkClientTests {

    private DingTalkClient dingTalkClient;

    @Before
    public void init(){
        String accessToken = "1676877387c8a486a8c81491def78a931d0a7df65e3eff0c3ef28c4a25b5cc50";
        String secretKey = "SEC92e1603e992a14af2b27efdb6753bf08eadc694ee6dfc9cb458327899dbd2695";
        dingTalkClient = new DingTalkClientImpl(accessToken, secretKey);
    }

    @Test
    public void sendTextTest(){
        dingTalkClient.sendText("简单测试");
        dingTalkClient.sendText("安特所有人测试", true);
        dingTalkClient.sendText("安特指定人测试", "18079637336");
    }

    @Test
    public void sendMarkdownTests(){
        // @所有人
        dingTalkClient.sendMarkdown("标题","" +
                "![screenshot](@lADOpwk3K80C0M0FoA)\n" +
                "### 李白\n" +
                "> 一位唐朝诗人！",
                true);

        // @某些人
        dingTalkClient.sendMarkdown("您有一条消息","" +
                        "![screenshot](@lADOpwk3K80C0M0FoA)\n" +
                        "### 李白\n" +
                        "> 一位唐朝诗人！@18079637336",
                "18079637336");
    }

    @Test
    public void sendLinkTests(){
        dingTalkClient.sendLink("好消息！好消息！","本群与百度成功达成合作关系，今后大家有什么不懂的可以直接百度搜索，不用再群里提问浪费时间啦！","https://www.baidu.com","http://www.baidu.com/img/bd_logo1.png", true);
        dingTalkClient.sendLink("好消息！好消息！","本群与百度成功达成合作关系，今后大家有什么不懂的可以直接百度搜索，不用再群里提问浪费时间啦！","https://www.baidu.com","http://www.baidu.com/img/bd_logo1.png");
    }

    @Test
    public void sendActionCardTests(){
        ActionCardDingTalkRequest request = new ActionCardDingTalkRequest();
        request.setTitle("标题");
        request.setMarkdownText("![screenshot](@lADOpwk3K80C0M0FoA)\n### 乔布斯20年前想打造一间苹果咖啡厅，而它正是AppleStore的前身 @18079637336");
        request.addBtn("内容不错", "https://www.cnblogs.com/kancy/p/13470386.html");
        request.addBtn("不感兴趣", "https://www.cnblogs.com/kancy/p/13912443.html");
        request.atMobile("18079637336");
        dingTalkClient.send(request);
    }

    @Test
    public void sendFeedCardTests(){
        FeedCardDingTalkRequest request = new FeedCardDingTalkRequest();
        request.addFirstLink("定位占用CPU较高的进程、线程、代码位置？", "https://www.cnblogs.com/kancy/p/13470386.html", "https://img1.baidu.com/it/u=3312920655,3266355600&fm=26&fmt=auto");
        request.addLink("浅谈我对DDD领域驱动设计的理解", "https://www.cnblogs.com/kancy/p/13425737.html");
        request.addLink("单元测试之PowerMock", "https://www.cnblogs.com/kancy/p/13912443.html");
        request.addLink("正确创建索引和索引失效", "https://www.cnblogs.com/kancy/p/13460140.html");
        request.atAll();
        dingTalkClient.send(request);
    }
}
