package com.selfProject.SearchOffline.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImageFileEnum {
    USER("IMAGE_USER"),
    PRODUCT("IMAGE_PRODUCT"),
    REVIEW("IMAGE_REVIEW"),
    MARKET("IMAGE_MARKET");

    private final String value;
}