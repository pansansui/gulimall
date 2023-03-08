package com.papan.gulimall.auth_server.controller;

import com.alibaba.fastjson.JSON;
import com.panpan.common.utils.HttpUtils;
import com.papan.gulimall.auth_server.fegin.MemberFeign;
import com.papan.gulimall.auth_server.vo.GithubSuccessVo;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.net.jsse.PEMFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

/**
 * @author panpan
 * @create 2021-09-08 下午4:28
 */
@Controller("oauth2/")
public class OAuth2Controller {
    @Autowired
    MemberFeign memberFeign;
    @RequestMapping("github/success")
    public String githubLogin(@RequestParam String code) throws Exception {
        PEMFile pemFile = new PEMFile("E:\\GoogleBrowser\\gullimall.2021-09-08.private-key.pem");
        byte[] encoded = pemFile.getPrivateKey().getEncoded();
        HashMap<String, String> map = new HashMap<>();
        map.put("client_id","Iv1.cd172628282bb2b1");
        map.put("client_secret","f9b706ff265b1b82740f660db462a22167c5d695");
        map.put("code",code);
        map.put("redirect_uri","http://auth.gulimall.com/oauth2/github/success");
        HttpResponse post = HttpUtils.doPost(
                "https://github.com",
                "/login/oauth/access_token",
                "post",
                null,
                null,
                map);
        if(post.getStatusLine().getStatusCode()!=200){
            return "redirect:http://auth.gulimall.com";
        }else {
            GithubSuccessVo githubSuccessVo = JSON.parseObject(EntityUtils.toString(post.getEntity()), GithubSuccessVo.class);
            String access_token = githubSuccessVo.getAccess_token();
        }

        return "redirect:http://gulimall.com";
    }

}
