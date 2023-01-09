/**
 * @author VISTALL
 * @since 09/01/2023
 */
module com.jetbrains.lang.dart.analyzer.server {
  requires args4j;
  requires com.google.common;
  requires transitive json;
  requires org.apache.commons.lang3;

  requires java.management;

  exports com.google.dart.command.analyze;
  exports com.google.dart.command.analyze.test;
  exports com.google.dart.engine;
  exports com.google.dart.engine.ast;
  exports com.google.dart.engine.ast.visitor;
  exports com.google.dart.engine.constant;
  exports com.google.dart.engine.context;
  exports com.google.dart.engine.element;
  exports com.google.dart.engine.element.angular;
  exports com.google.dart.engine.element.polymer;
  exports com.google.dart.engine.element.visitor;
  exports com.google.dart.engine.error;
  exports com.google.dart.engine.html.ast;
  exports com.google.dart.engine.html.ast.visitor;
  exports com.google.dart.engine.html.parser;
  exports com.google.dart.engine.html.scanner;
  exports com.google.dart.engine.index;
  exports com.google.dart.engine.internal.builder;
  exports com.google.dart.engine.internal.cache;
  exports com.google.dart.engine.internal.constant;
  exports com.google.dart.engine.internal.context;
  exports com.google.dart.engine.internal.element;
  exports com.google.dart.engine.internal.element.angular;
  exports com.google.dart.engine.internal.element.handle;
  exports com.google.dart.engine.internal.element.member;
  exports com.google.dart.engine.internal.element.polymer;
  exports com.google.dart.engine.internal.error;
  exports com.google.dart.engine.internal.hint;
  exports com.google.dart.engine.internal.html;
  exports com.google.dart.engine.internal.html.angular;
  exports com.google.dart.engine.internal.html.polymer;
  exports com.google.dart.engine.internal.index;
  exports com.google.dart.engine.internal.index.operation;
  exports com.google.dart.engine.internal.object;
  exports com.google.dart.engine.internal.parser;
  exports com.google.dart.engine.internal.resolver;
  exports com.google.dart.engine.internal.scope;
  exports com.google.dart.engine.internal.sdk;
  exports com.google.dart.engine.internal.search;
  exports com.google.dart.engine.internal.search.listener;
  exports com.google.dart.engine.internal.search.pattern;
  exports com.google.dart.engine.internal.search.scope;
  exports com.google.dart.engine.internal.task;
  exports com.google.dart.engine.internal.type;
  exports com.google.dart.engine.internal.verifier;
  exports com.google.dart.engine.parser;
  exports com.google.dart.engine.resolver;
  exports com.google.dart.engine.scanner;
  exports com.google.dart.engine.sdk;
  exports com.google.dart.engine.search;
  exports com.google.dart.engine.source;
  exports com.google.dart.engine.type;
  exports com.google.dart.engine.utilities.ast;
  exports com.google.dart.engine.utilities.collection;
  exports com.google.dart.engine.utilities.dart;
  exports com.google.dart.engine.utilities.general;
  exports com.google.dart.engine.utilities.instrumentation;
  exports com.google.dart.engine.utilities.io;
  exports com.google.dart.engine.utilities.logging;
  exports com.google.dart.engine.utilities.os;
  exports com.google.dart.engine.utilities.source;
  exports com.google.dart.engine.utilities.translation;
}