package org.proto.sparkwebapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping
public class Service {

    @RequestMapping(method = RequestMethod.GET, value="/simple")
    public @ResponseBody String simple(@RequestParam(required = false) String param) {
        if (param == null) {
            return "simple";
        } else {
            return "simple - param=" + param;
        }
    }

    @RequestMapping(method = RequestMethod.GET, value="/upper")
    public @ResponseBody boolean upper(@RequestParam(required = true) String inputFile,
                                       @RequestParam(required = true) String outputDir,
                                       @RequestParam(required = false) String master,
                                       @RequestParam(required = false) String namenode) {
        if(master==null) master = "local";
        SparkUpper.upper(inputFile, outputDir, master, namenode);

        return true;
    }
}