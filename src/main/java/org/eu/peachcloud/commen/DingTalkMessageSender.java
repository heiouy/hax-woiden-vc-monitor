package org.eu.peachcloud.commen;

import cn.hutool.core.lang.Assert;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.taobao.api.ApiException;
import lombok.Data;
import org.apache.commons.codec.binary.Base64;
import org.eu.peachcloud.config.MonitorConfig;
import org.eu.peachcloud.enums.ChannelEnum;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author nike
 * 2023/04/12/20:45
 * 钉钉消息发送器
 */
@Data
public class DingTalkMessageSender {
    private final MonitorConfig config = ConfigFactory.getConfigInstance();

    private String title;

    private String text;

    public void buildText(List<String> list, ChannelEnum channelEnum) {
        Assert.notNull(list, "钉钉发送消息实体类列表为空");
        StringBuilder builder = new StringBuilder();
        builder.append("#### **").append(channelEnum.getName()).append("库存**").append("\n");
        for (String machine : list) {
            builder.append("- ").append(machine).append("\n");
        }
        this.text = builder.toString();
    }

    public void send() {
        Long timestamp = System.currentTimeMillis();
        String sign = makeSecret(timestamp);
        String url = "" + config.getOpenApi() +
                "?access_token=" + config.getAccessToken() + "&timestamp=" + timestamp + "&sign=" + sign + "";
        DefaultDingTalkClient client = new DefaultDingTalkClient(url);
        client.isUseJsonString();
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("markdown");
        OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
        markdown.setTitle(title);
        markdown.setText(text);
        request.setMarkdown(markdown);
        try {
            client.execute(request);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    private String makeSecret(Long timestamp) {
        String charsetName = "UTF-8";
        String algorithm = "HmacSHA256";
        String secret = config.getSecret();
        String stringToSign = timestamp + "\n" + secret;
        String sign = null;
        try {
            Mac mac = Mac.getInstance(algorithm);
            mac.init(new SecretKeySpec(secret.getBytes(charsetName), algorithm));
            byte[] signData = mac.doFinal(stringToSign.getBytes(charsetName));
            sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), charsetName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sign;
    }
}
