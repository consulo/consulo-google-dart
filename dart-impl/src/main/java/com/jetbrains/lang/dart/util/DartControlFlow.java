package com.jetbrains.lang.dart.util;

import com.jetbrains.lang.dart.psi.*;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiRecursiveElementVisitor;
import consulo.language.psi.PsiReference;
import consulo.language.psi.scope.LocalSearchScope;
import consulo.language.psi.search.ReferencesSearch;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.util.collection.ContainerUtil;
import consulo.util.lang.function.Condition;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Fedor.Korotkov
 */
public class DartControlFlow {

  private List<DartComponentName> myParameters;
  private final List<DartComponentName> myReturnValues;

  protected DartControlFlow(List<DartComponentName> inComponentNames, List<DartComponentName> outDeclarations) {
    myParameters = inComponentNames;
    myReturnValues = outDeclarations;
  }

  public List<DartComponentName> getParameters() {
    return myParameters;
  }

  public List<DartComponentName> getReturnValues() {
    return myReturnValues;
  }

  public static DartControlFlow analyze(PsiElement[] elements) {
    final PsiElement scope = PsiTreeUtil.getTopmostParentOfType(elements[0], DartExecutionScope.class);
    final PsiElement lastElement = elements[elements.length - 1];
    final int lastElementEndOffset = lastElement.getTextOffset() + lastElement.getTextLength();
    final int firstElementStartOffset = elements[0].getTextOffset();

    // find out params
    assert scope != null;
    final LocalSearchScope localSearchScope = new LocalSearchScope(scope);
    final List<DartComponentName> outDeclarations = ContainerUtil.filter(
      DartControlFlowUtil.getSimpleDeclarations(elements, null, false),
      new Condition<DartComponentName>() {
        @Override
        public boolean value(DartComponentName componentName) {
          for (PsiReference usage : ReferencesSearch.search(componentName, localSearchScope, false).findAll()) {
            if (usage.getElement().getTextOffset() > lastElementEndOffset) {
              return true;
            }
          }
          return false;
        }
      });

    // find params
    final DartReferenceVisitor dartReferenceVisitor = new DartReferenceVisitor();
    for (PsiElement element : elements) {
      element.accept(dartReferenceVisitor);
    }
    final List<DartComponentName> inComponentNames = ContainerUtil.filter(
      dartReferenceVisitor.getComponentNames(), new consulo.util.lang.function.Condition<DartComponentName>() {
        @Override
        public boolean value(DartComponentName componentName) {
          final int offset = componentName.getTextOffset();
          final boolean declarationInElements = firstElementStartOffset <= offset && offset < lastElementEndOffset;
          return !declarationInElements;
        }
      });


    return new DartControlFlow(inComponentNames, outDeclarations);
  }

  public void filterParams(consulo.util.lang.function.Condition<? super DartComponentName> condition) {
    myParameters = ContainerUtil.filter(myParameters, condition);
  }

  public String getReplaceStatementText(String functionName) {
    final StringBuilder result = new StringBuilder();
    if (!myReturnValues.isEmpty()) {
      final DartComponentName componentName = myReturnValues.iterator().next();
      result.append("var ");
      result.append(componentName.getName());
      result.append(" = ");
    }
    result.append(getSignature(functionName, false));
    return result.toString();
  }

  private void appendFirstReturnTypeName(StringBuilder result) {
    final DartComponentName componentName = myReturnValues.iterator().next();
    final DartClassResolveResult resolveResult = DartResolveUtil.getDartClassResolveResult(componentName);
    final DartClass dartClass = resolveResult.getDartClass();
    if (dartClass != null) {
      result.append(DartPresentableUtil.buildClassText(dartClass, resolveResult.getSpecialization()));
      result.append(" ");
    }
  }

  public String getSignature(String functionName) {
    return getSignature(functionName, true);
  }

  private String getSignature(String functionName, boolean declaration) {
    final StringBuilder result = new StringBuilder();
    if (declaration && !myReturnValues.isEmpty()) {
      appendFirstReturnTypeName(result);
    }
    result.append(functionName);
    result.append("(");
    for (int i = 0; i < myParameters.size(); i++) {
      if (i > 0) result.append(", ");
      DartComponentName componentName = myParameters.get(i);
      final DartClassResolveResult resolveResult = DartResolveUtil.getDartClassResolveResult(componentName);
      final DartClass dartClass = resolveResult.getDartClass();
      if (declaration && dartClass != null) {
        final String typeText = DartPresentableUtil.buildClassText(dartClass, resolveResult.getSpecialization());
        result.append(typeText).append(" ");
      }
      result.append(componentName.getName());
    }
    result.append(")");
    return result.toString();
  }

  private static class DartReferenceVisitor extends PsiRecursiveElementVisitor {
    private final List<DartComponentName> myComponentNames = new ArrayList<DartComponentName>();

    public List<DartComponentName> getComponentNames() {
      return myComponentNames;
    }

    @Override
    public void visitElement(PsiElement element) {
      if (element instanceof DartType) {
        // ignore types
        return;
      }
      if (element instanceof DartReference &&
        DartResolveUtil.aloneOrFirstInChain((DartReference)element)) {
        final PsiElement resolve = ((DartReference)element).resolve();
        if (resolve instanceof DartComponentName && !myComponentNames.contains((DartComponentName)resolve)) {
          myComponentNames.add((DartComponentName)resolve);
        }
      }
      super.visitElement(element);
    }
  }
}
