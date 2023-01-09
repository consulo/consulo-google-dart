/**
 * @author VISTALL
 * @since 09/01/2023
 */
module com.jetbrains.lang.dart {
  requires consulo.ide.api;

  requires com.jetbrains.lang.dart.analyzer.server;

  requires com.intellij.xml;
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