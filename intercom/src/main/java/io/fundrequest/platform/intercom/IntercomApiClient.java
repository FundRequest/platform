package io.fundrequest.platform.intercom;

import io.intercom.api.AdminMessage;
import io.intercom.api.Event;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient(name = "intercom-api-client",
             url = "https://api.intercom.io/",
             configuration = IntercomFeignConfiguration.class)
public interface IntercomApiClient {

    @RequestMapping(value = "/events", method = POST, headers = "Content-Type=application/json")
    void postEvent(Event event);

    @RequestMapping(value = "/messages", method = POST, headers = {"Content-Type=application/json", "Accept=application/json"})
    void postMessage(AdminMessage message);
}
