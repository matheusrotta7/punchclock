package com.punchy.punchclock.controller;

import com.punchy.punchclock.entity.Punch;
import com.punchy.punchclock.exception.PunchException;
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
    public ResponseEntity<Punch> createPunch(@RequestBody Punch punchBody) {
        try {
            Punch punch = punchService.createPunch(punchBody);
            return ResponseEntity.ok(punch);
        } catch (PunchException pe) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping
    public ResponseEntity<Void> updatePunch(@RequestBody Punch punchBody) {
        try {
            punchService.updatePunch(punchBody);
            return ResponseEntity.ok().build();
        } catch (PunchException pe) {
            pe.printStackTrace();
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
