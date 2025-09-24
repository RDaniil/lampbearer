package com.vdn.lampbearer.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.builder.graphics.CharacterTileStringBuilder;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.component.Component;
import org.hexworks.zircon.api.component.LogArea;

@Slf4j
@RequiredArgsConstructor
public class Log  {
    private final LogArea logArea;

    public void addParagraph(String text, boolean b, int i) {
        logArea.addParagraph(text, b, i);
    }

    public void addParagraph(String text){
        logArea.addParagraph(text, false, 0);
    }

    public void addInlineText(String text) {
        addInlineText(0, text);
    }

    public void addInlineText(int lengthAlreadyInLine, String text) {
        int maxLength = 60 - lengthAlreadyInLine;
        int numberOfSubstrings = (text.length() + maxLength - 1)
                / maxLength;
        if(numberOfSubstrings < 2){
            logArea.addInlineText(text);
            return;
        }

        for (int i = 0; i < numberOfSubstrings; i++) {
            int startIndex = i * maxLength;
            int endIndex = Math.min(startIndex + maxLength, text.length());

            String substring = text.substring(startIndex, endIndex);
            logArea.addInlineText(substring);
            commitInlineElements();
        }
    }

    public void addInlineComponent(String text, TileColor color){
        //Костыль для сломанного ключа
        if(color.getBlue() == 17 && color.getGreen() == 17 && color.getRed() == 17){
            color = TileColor.fromString("#f12d23");
        }
        TileColor finalColor = color;
        logArea
            .addInlineComponent(
                Components.label()
                    .withText(text)
                    .withComponentRenderer((graphics, cntxt) -> {
                        var bang = CharacterTileStringBuilder.newBuilder()
                                .withText(text)
                                .withForegroundColor(finalColor)
                                .build();
                        graphics.draw(bang);
                    }).build());
    }

    public void addInlineComponent(String text, TileColor foreground, TileColor background){
        logArea
            .addInlineComponent(
                Components.label()
                    .withText(text)
                    .withComponentRenderer((graphics, cntxt) -> {
                        var bang = CharacterTileStringBuilder.newBuilder()
                                .withText(text)
                                .withForegroundColor(foreground)
                                .withBackgroundColor(background)
                                .build();
                        graphics.draw(bang);
                    }).build());
    }

    public void commitInlineElements(){
        logArea.commitInlineElements();
    }

    public void addInlineComponent(Component component) {
        logArea.addInlineComponent(component);
    }

}
