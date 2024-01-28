package com.freeing.id.rest.webmvc.controller;

import com.freeing.id.core.bean.Id;
import com.freeing.id.manager.IdManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IdController {
    @Autowired
    private IdManager idManager;

    @RequestMapping("/genid")
    public long genId() {
        return idManager.genId();
    }

    @RequestMapping("/expid")
    public Id explainId(@RequestParam(value = "id", defaultValue = "0") long id) {
        return idManager.expId(id);
    }

    @RequestMapping("/transtime")
    public String transTime(
        @RequestParam(value = "time", defaultValue = "-1") long time) {
        return idManager.transTime(time).toString();
    }

    @RequestMapping("/makeid")
    public long makeId(
        @RequestParam(value = "version", defaultValue = "-1") long version,
        @RequestParam(value = "type", defaultValue = "-1") long type,
        @RequestParam(value = "genMethod", defaultValue = "-1") long genMethod,
        @RequestParam(value = "machine", defaultValue = "-1") long machine,
        @RequestParam(value = "time", defaultValue = "-1") long time,
        @RequestParam(value = "seq", defaultValue = "-1") long seq) {

        long madeId;
        if (time == -1 || seq == -1)
            throw new IllegalArgumentException("Both time and seq are required.");
        else if (version == -1) {
            if (type == -1) {
                if (genMethod == -1) {
                    madeId = machine == -1 ? idManager.makeId(time, seq) : idManager.makeId(time, seq, machine);
                } else {
                    madeId = idManager.makeId(genMethod, time, seq, machine);
                }
            } else {
                madeId = idManager.makeId(type, genMethod, time, seq, machine);
            }
        } else {
            madeId = idManager.makeId(version, type, genMethod, time, seq, machine);
        }
        return madeId;
    }
}
