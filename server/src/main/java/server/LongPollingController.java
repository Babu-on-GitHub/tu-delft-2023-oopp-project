package server;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.annotation.PreDestroy;
import java.util.ArrayList;


@RestController
public class LongPollingController {

    private ArrayList<DeferredResult<String>> results = new ArrayList<>();
    @RequestMapping(value = "/long/status", method = RequestMethod.GET)
    @ResponseBody
    public DeferredResult<String> longPolling() {
        var newResult = new DeferredResult<String>(5000L);
        results.add(newResult);
        newResult.setResult("OK");
        newResult.onCompletion(() -> results.remove(newResult));
        return newResult;
    }

    @PreDestroy
    public void cleanUp() {
        for (var result : results) {
            result.setResult("STOP");
        }
        results.clear();
    }

}
