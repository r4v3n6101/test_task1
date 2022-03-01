package request;

import feign.RequestLine;

public interface SumSubApi {

    /**
     * Example request for status checking
     *
     * @return empty JSON object or `{}` string
     */
    @RequestLine("GET /resources/status/api")
    String health();
}

