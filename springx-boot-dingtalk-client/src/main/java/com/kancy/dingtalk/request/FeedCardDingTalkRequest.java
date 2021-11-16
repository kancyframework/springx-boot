package com.kancy.dingtalk.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * FeedCardDingTalkRequest
 *
 * @author huangchengkang
 * @date 2021/11/16 10:35
 */
public final class FeedCardDingTalkRequest extends DingTalkRequest{

    private FeedCard feedCard;

    public FeedCardDingTalkRequest() {
        super("feedCard");
        this.feedCard = new FeedCard();
        this.feedCard.setLinks(new ArrayList<>());
    }

    public void addLink(String title, String messageURL) {
        this.feedCard.links.add(new Link(title, messageURL));
    }

    public void addLink(String title, String messageURL, String picURL) {
        this.feedCard.links.add(new Link(title, messageURL, picURL));
    }

    public void addLink(String title, String messageURL, Integer index) {
        this.feedCard.links.add(index, new Link(title, messageURL));
    }

    public void addLink(String title, String messageURL, String picURL, Integer index) {
        this.feedCard.links.add(index, new Link(title, messageURL, picURL));
    }

    public void addFirstLink(String title, String messageURL) {
        addLink(title, messageURL, 0);
    }

    public void addFirstLink(String title, String messageURL, String picURL) {
        addLink(title, messageURL, picURL,0);
    }

    private static class FeedCard {
        private List<Link> links;

        public void setLinks(List<Link> links) {
            this.links = links;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");

            StringBuilder linkSb = new StringBuilder();
            if (!links.isEmpty()){
                linkSb.append("[");
                for (Link btn : links) {
                    linkSb.append(btn).append(",");
                }
                linkSb.deleteCharAt(linkSb.length()-1);
                linkSb.append("]");
            } else {
                linkSb.append("[]");
            }
            sb.append('"').append("links").append('"').append(":").append(linkSb);
            sb.append("}");
            return sb.toString();
        }
    }

    static class Link {
        private String title;
        private String messageURL;
        private String picURL;

        public Link(String title, String messageURL) {
            this.title = title;
            this.messageURL = messageURL;
        }

        public Link(String title, String messageURL, String picURL) {
            this.title = title;
            this.messageURL = messageURL;
            this.picURL = picURL;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append('"').append("title").append('"').append(":").append('"').append(title).append('"');
            if (notEmpty(messageURL)){
                sb.append(",").append('"').append("messageURL").append('"').append(":").append('"').append(messageURL).append('"');
            }
            if (notEmpty(picURL)){
                sb.append(",").append('"').append("picURL").append('"').append(":").append('"').append(picURL).append('"');
            }
            sb.append("}");
            return sb.toString();
        }

        private boolean notEmpty(String str) {
            return Objects.nonNull(str) && !str.isEmpty();
        }
    }

    @Override
    public String toString() {
        return toStringBuilder(feedCard);
    }
}
