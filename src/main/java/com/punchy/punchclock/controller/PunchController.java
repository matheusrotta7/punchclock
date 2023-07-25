package com.punchy.punchclock.controller;

import com.punchy.punchclock.entity.Punch;
import com.punchy.punchclock.service.PunchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/punch")
@CrossOrigin
public class PunchController {

    @Autowired
    private PunchService punchService;

    @GetMapping("/{id}")
    public ResponseEntity<Punch> getPunch(@PathVariable("id") Long id) {
        Punch punch = punchService.getPunchById(id);
        if (punch != null) {
            return ResponseEntity.ok(punch);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Punch createPunch(@RequestBody Punch punchBody) {
        return punchService.createPunch(punchBody);
    }

    @PutMapping
    public ResponseEntity<Punch> updatePunch(@RequestBody Punch punchBody) {
        try {
            return ResponseEntity.ok(punchService.updatePunch(punchBody));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}
