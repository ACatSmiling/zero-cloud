package cn.zero.cloud.redis.redlock.controller;

import cn.zero.cloud.redis.redlock.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Xisun Wang
 * @since 2024/6/21 22:20
 */
@RestController
@RequestMapping(value = "/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping(value = "/saleByRedlock", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String saleByRedlock() {
        return inventoryService.saleByRedlock();
    }
}
