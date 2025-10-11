package com.jetbrains.lang.dart.ide.actions;

import consulo.google.dart.localize.DartLocalize;
import consulo.localize.LocalizeValue;

public class DartPubBuildAction extends DartPubActionBase {
    @Override
    protected LocalizeValue getPresentableText() {
        return DartLocalize.dartPubBuild();
    }

    @Override
    protected String getPubCommand() {
        return "build";
    }

    @Override
    protected LocalizeValue getSuccessMessage() {
        return DartLocalize.dartPubBuildSuccess();
    }
}
