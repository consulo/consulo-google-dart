/**
 * @author VISTALL
 * @since 09/01/2023
 */
module com.jetbrains.lang.dart {
  requires consulo.application.api;
  requires consulo.application.content.api;
  requires consulo.application.ui.api;
  requires consulo.base.icon.library;
  requires consulo.code.editor.api;
  requires consulo.color.scheme.api;
  requires consulo.component.api;
  requires consulo.configurable.api;
  requires consulo.datacontext.api;
  requires consulo.disposer.api;
  requires consulo.document.api;
  requires consulo.execution.api;
  requires consulo.execution.debug.api;
  requires consulo.execution.test.api;
  requires consulo.execution.test.sm.api;
  requires consulo.file.chooser.api;
  requires consulo.file.editor.api;
  requires consulo.file.template.api;
  requires consulo.ide.api;
  requires consulo.index.io;
  requires consulo.language.api;
  requires consulo.language.code.style.api;
  requires consulo.language.code.style.ui.api;
  requires consulo.language.copyright.api;
  requires consulo.language.editor.api;
  requires consulo.language.editor.refactoring.api;
  requires consulo.language.editor.ui.api;
  requires consulo.language.impl;
  requires consulo.localize.api;
  requires consulo.logging.api;
  requires consulo.module.api;
  requires consulo.module.content.api;
  requires consulo.module.ui.api;
  requires consulo.navigation.api;
  requires consulo.platform.api;
  requires consulo.process.api;
  requires consulo.project.api;
  requires consulo.project.ui.api;
  requires consulo.ui.api;
  requires consulo.ui.ex.api;
  requires consulo.ui.ex.awt.api;
  requires consulo.undo.redo.api;
  requires consulo.usage.api;
  requires consulo.util.collection;
  requires consulo.util.dataholder;
  requires consulo.util.io;
  requires consulo.util.lang;
  requires consulo.util.xml.serializer;
  requires consulo.virtual.file.system.api;

  requires com.jetbrains.lang.dart.analyzer.server;

  requires com.intellij.xml;
  requires com.intellij.xml.html.api;
  requires org.yaml.snakeyaml;
  requires consulo.library.flexmark;
  requires org.jetbrains.plugins.yaml;

  requires forms.rt;

  // TODO remove in future
  requires java.desktop;

  exports com.jetbrains.lang.dart;
  exports com.jetbrains.lang.dart.analyzer;
  exports com.jetbrains.lang.dart.highlight;
  exports com.jetbrains.lang.dart.ide;
  exports com.jetbrains.lang.dart.ide.actions;
  exports com.jetbrains.lang.dart.ide.actions.ui;
  exports com.jetbrains.lang.dart.ide.annotator;
  exports com.jetbrains.lang.dart.ide.completion;
  exports com.jetbrains.lang.dart.ide.copyright;
  exports com.jetbrains.lang.dart.ide.documentation;
  exports com.jetbrains.lang.dart.ide.editor;
  exports com.jetbrains.lang.dart.ide.findUsages;
  exports com.jetbrains.lang.dart.ide.folding;
  exports com.jetbrains.lang.dart.ide.formatter;
  exports com.jetbrains.lang.dart.ide.formatter.settings;
  exports com.jetbrains.lang.dart.ide.generation;
  exports com.jetbrains.lang.dart.ide.index;
  exports com.jetbrains.lang.dart.ide.info;
  exports com.jetbrains.lang.dart.ide.inspections;
  exports com.jetbrains.lang.dart.ide.inspections.analyzer;
  exports com.jetbrains.lang.dart.ide.marker;
  exports com.jetbrains.lang.dart.ide.refactoring;
  exports com.jetbrains.lang.dart.ide.refactoring.extract;
  exports com.jetbrains.lang.dart.ide.refactoring.introduce;
  exports com.jetbrains.lang.dart.ide.runner;
  exports com.jetbrains.lang.dart.ide.runner.base;
  exports com.jetbrains.lang.dart.ide.runner.server;
  exports com.jetbrains.lang.dart.ide.runner.server.frame;
  exports com.jetbrains.lang.dart.ide.runner.server.google;
  exports com.jetbrains.lang.dart.ide.runner.server.ui;
  exports com.jetbrains.lang.dart.ide.runner.unittest;
  exports com.jetbrains.lang.dart.ide.runner.unittest.ui;
  exports com.jetbrains.lang.dart.ide.settings;
  exports com.jetbrains.lang.dart.ide.structure;
  exports com.jetbrains.lang.dart.ide.surroundWith;
  exports com.jetbrains.lang.dart.ide.surroundWith.expression;
  exports com.jetbrains.lang.dart.ide.surroundWith.statement;
  exports com.jetbrains.lang.dart.ide.template;
  exports com.jetbrains.lang.dart.ide.template.macro;
  exports com.jetbrains.lang.dart.lexer;
  exports com.jetbrains.lang.dart.psi;
  exports com.jetbrains.lang.dart.psi.impl;
  exports com.jetbrains.lang.dart.resolve;
  exports com.jetbrains.lang.dart.sdk;
  exports com.jetbrains.lang.dart.sdk.listPackageDirs;
  exports com.jetbrains.lang.dart.util;
  exports com.jetbrains.lang.dart.validation.fixes;
  exports consulo.dart;
  exports consulo.dart.debugger.breakpoint;
  exports consulo.dart.module.extension;
  exports consulo.google.dart.icon;
}
