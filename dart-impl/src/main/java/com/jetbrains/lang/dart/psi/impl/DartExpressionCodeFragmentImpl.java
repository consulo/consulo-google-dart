package com.jetbrains.lang.dart.psi.impl;

import com.jetbrains.lang.dart.DartLanguage;
import com.jetbrains.lang.dart.DartParser;
import com.jetbrains.lang.dart.DartTokenTypes;
import com.jetbrains.lang.dart.psi.DartExpressionCodeFragment;
import com.jetbrains.lang.dart.psi.DartFile;
import consulo.language.Language;
import consulo.language.ast.ASTNode;
import consulo.language.ast.IFileElementType;
import consulo.language.file.FileTypeManager;
import consulo.language.file.FileViewProvider;
import consulo.language.file.light.LightVirtualFile;
import consulo.language.impl.ast.FileElement;
import consulo.language.impl.ast.TreeElement;
import consulo.language.impl.file.SingleRootFileViewProvider;
import consulo.language.parser.PsiBuilder;
import consulo.language.parser.PsiBuilderFactory;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiManager;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.version.LanguageVersionUtil;
import consulo.project.Project;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import static consulo.language.impl.parser.GeneratedParserUtilBase.*;

public class DartExpressionCodeFragmentImpl extends DartFile implements DartExpressionCodeFragment {
  private PsiElement myContext;
  private boolean myPhysical;
  private FileViewProvider myViewProvider;
  private GlobalSearchScope myScope = null;

  public DartExpressionCodeFragmentImpl(Project project, String name, CharSequence text, boolean isPhysical) {
    super(new SingleRootFileViewProvider(PsiManager.getInstance(project), new LightVirtualFile(name,
                                                                                               FileTypeManager.getInstance()
                                                                                                              .getFileTypeByFileName(name),
                                                                                               text), isPhysical) {
      @Override
      public boolean supportsIncrementalReparse(@Nonnull Language rootLanguage) {
        return false;
      }
    });

    myPhysical = isPhysical;
    ((SingleRootFileViewProvider)getViewProvider()).forceCachedPsi(this);
    final DartFragmentElementType type = new DartFragmentElementType();
    init(type, type);
  }


  public PsiElement getContext() {
    return myContext;
  }

  @Nonnull
  public FileViewProvider getViewProvider() {
    if (myViewProvider != null) {
      return myViewProvider;
    }
    return super.getViewProvider();
  }

  public boolean isValid() {
    if (!super.isValid()) {
      return false;
    }
    if (myContext != null && !myContext.isValid()) {
      return false;
    }
    return true;
  }

  protected DartExpressionCodeFragmentImpl clone() {
    final DartExpressionCodeFragmentImpl clone = (DartExpressionCodeFragmentImpl)cloneImpl((FileElement)calcTreeElement().clone());
    clone.myPhysical = myPhysical;
    clone.myOriginalFile = this;
    final SingleRootFileViewProvider cloneViewProvider =
      new SingleRootFileViewProvider(getManager(), new LightVirtualFile(getName(), getLanguage(), getText()), myPhysical);
    clone.myViewProvider = cloneViewProvider;
    cloneViewProvider.forceCachedPsi(clone);
    clone.init(getContentElementType(), getContentElementType());
    return clone;
  }

  public boolean isPhysical() {
    return myPhysical;
  }

  public void setContext(PsiElement context) {
    myContext = context;
  }

  @Override
  public void forceResolveScope(GlobalSearchScope scope) {
    myScope = scope;
  }

  @Override
  public GlobalSearchScope getForcedResolveScope() {
    return myScope;
  }

  private static class DartFragmentElementType extends IFileElementType {
    public DartFragmentElementType() {
      super("DART_CODE_FRAGMENT", DartLanguage.INSTANCE);
    }

    @Nullable
    @Override
    public ASTNode parseContents(final ASTNode chameleon) {
      final PsiElement psi = new DartPsiCompositeElementImpl(chameleon);
      return doParseContents(chameleon, psi);
    }

    @Override
    protected ASTNode doParseContents(@Nonnull ASTNode chameleon, @Nonnull PsiElement psi) {
      final PsiBuilderFactory factory = PsiBuilderFactory.getInstance();
      final PsiBuilder psiBuilder = factory.createBuilder(((TreeElement)chameleon).getManager().getProject(), chameleon,
                                                          LanguageVersionUtil.findDefaultVersion(getLanguage()));
      final PsiBuilder builder = adapt_builder_(DartTokenTypes.STATEMENTS, psiBuilder, new DartParser(), DartParser.EXTENDS_SETS_);

      PsiBuilder.Marker marker = enter_section_(builder, 0, _COLLAPSE_, "<code fragment>");
      boolean result = DartParser.expression(builder, 0);
      exit_section_(builder, 0, marker, DartTokenTypes.STATEMENTS, result, true, TRUE_CONDITION);
      return builder.getTreeBuilt();
    }
  }
}
