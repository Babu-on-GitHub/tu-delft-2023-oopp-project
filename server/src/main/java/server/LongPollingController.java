package server;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;


@RestController
public class LongPollingController {

    @RequestMapping(value = "/long/status", method = RequestMethod.GET)
    @ResponseBody
    public DeferredResult<String> longPolling() {
        DeferredResult<String> deferredResult = new DeferredResult<>(5000L);
        deferredResult.setResult("OK");
        return deferredResult;
    }
}
