package nl.kubebit.core.infrastructure.common.controller;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 
 */
@Profile("!nocache")
@Tag(name = "Cache")
@RestController
@RequestMapping("/api/v1/cache")
public class CacheController {
    // --------------------------------------------------------------------------------------------

    //
    private final CacheManager cacheManager;

    /**
     *
     */
    public CacheController(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * 
     */
    @PostMapping("/clear")
    public void clear() {
        cacheManager.getCacheNames().forEach(name -> Objects.requireNonNull(cacheManager.getCache(name)).clear());
    }

    /**
     * 
     */
    @GetMapping
    public List<Object> show() {
        return cacheManager.getCacheNames().stream().map(name -> Objects.requireNonNull(cacheManager.getCache(name)).getNativeCache()).collect(Collectors.toList());
    }
}