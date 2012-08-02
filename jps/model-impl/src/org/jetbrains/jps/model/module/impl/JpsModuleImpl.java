package org.jetbrains.jps.model.module.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.model.*;
import org.jetbrains.jps.model.impl.*;
import org.jetbrains.jps.model.library.*;
import org.jetbrains.jps.model.library.impl.JpsLibraryCollectionImpl;
import org.jetbrains.jps.model.library.impl.JpsLibraryRole;
import org.jetbrains.jps.model.module.*;

import java.util.List;

/**
 * @author nik
 */
public class JpsModuleImpl extends JpsNamedCompositeElementBase<JpsModuleImpl> implements JpsModule {
  private static final JpsTypedDataRole<JpsModuleType<?>> TYPED_DATA_ROLE = new JpsTypedDataRole<JpsModuleType<?>>();
  private static final JpsUrlListRole CONTENT_ROOTS_ROLE = new JpsUrlListRole("content roots");
  private static final JpsUrlListRole EXCLUDED_ROOTS_ROLE = new JpsUrlListRole("excluded roots");
  public static final JpsElementChildRole<JpsDependenciesListImpl>
    DEPENDENCIES_LIST_CHILD_ROLE = JpsElementChildRoleBase.create("dependencies");
  private final JpsLibraryCollection myLibraryCollection;

  public <P extends JpsElementProperties> JpsModuleImpl(JpsModuleType<P> type, @NotNull String name, @NotNull P properties) {
    super(name);
    myContainer.setChild(TYPED_DATA_ROLE, new JpsTypedDataImpl<JpsModuleType<?>>(type, properties));
    myContainer.setChild(CONTENT_ROOTS_ROLE);
    myContainer.setChild(EXCLUDED_ROOTS_ROLE);
    myContainer.setChild(JpsFacetRole.COLLECTION_ROLE);
    myContainer.setChild(DEPENDENCIES_LIST_CHILD_ROLE, new JpsDependenciesListImpl());
    myLibraryCollection = new JpsLibraryCollectionImpl(myContainer.setChild(JpsLibraryRole.LIBRARIES_COLLECTION_ROLE));
    myContainer.setChild(JpsModuleSourceRootRole.ROOT_COLLECTION_ROLE);
    myContainer.setChild(JpsSdkReferencesTableImpl.ROLE);
  }

  private JpsModuleImpl(JpsModuleImpl original) {
    super(original);
    myLibraryCollection = new JpsLibraryCollectionImpl(myContainer.getChild(JpsLibraryRole.LIBRARIES_COLLECTION_ROLE));
  }

  @NotNull
  @Override
  public JpsModuleImpl createCopy() {
    return new JpsModuleImpl(this);
  }

  @Override
  @NotNull
  public JpsElementProperties getProperties() {
    return myContainer.getChild(TYPED_DATA_ROLE).getProperties();
  }

  @NotNull
  @Override
  public JpsUrlList getContentRootsList() {
    return myContainer.getChild(CONTENT_ROOTS_ROLE);
  }

  @NotNull
  public JpsUrlList getExcludeRootsList() {
    return myContainer.getChild(EXCLUDED_ROOTS_ROLE);
  }

  @NotNull
  @Override
  public List<JpsModuleSourceRoot> getSourceRoots() {
    return myContainer.getChild(JpsModuleSourceRootRole.ROOT_COLLECTION_ROLE).getElements();
  }

  @NotNull
  @Override
  public <P extends JpsElementProperties, T extends JpsModuleSourceRootType<P> & JpsElementTypeWithDefaultProperties<P>>
  JpsModuleSourceRoot addSourceRoot(@NotNull String url, @NotNull T rootType) {
    return addSourceRoot(url, rootType, rootType.createDefaultProperties());
  }

  @NotNull
  @Override
  public <P extends JpsElementProperties> JpsModuleSourceRoot addSourceRoot(@NotNull String url,
                                                                            @NotNull JpsModuleSourceRootType<P> rootType,
                                                                            @NotNull P properties) {
    final JpsModuleSourceRootImpl root = new JpsModuleSourceRootImpl(url, rootType, properties);
    myContainer.getChild(JpsModuleSourceRootRole.ROOT_COLLECTION_ROLE).addChild(root);
    root.setProperties(rootType, properties);
    return root;
  }

  @Override
  public void removeSourceRoot(@NotNull String url, @NotNull JpsModuleSourceRootType rootType) {
    final JpsElementCollectionImpl<JpsModuleSourceRoot> roots = myContainer.getChild(JpsModuleSourceRootRole.ROOT_COLLECTION_ROLE);
    for (JpsModuleSourceRoot root : roots.getElements()) {
      if (root.getRootType().equals(rootType) && root.getUrl().equals(url)) {
        roots.removeChild(root);
        break;
      }
    }
  }

  @NotNull
  @Override
  public <P extends JpsElementProperties> JpsFacet addFacet(@NotNull String name, @NotNull JpsFacetType<P> type, @NotNull P properties) {
    return myContainer.getChild(JpsFacetRole.COLLECTION_ROLE).addChild(new JpsFacetImpl(type, name, properties));
  }

  @NotNull
  @Override
  public List<JpsFacet> getFacets() {
    return myContainer.getChild(JpsFacetRole.COLLECTION_ROLE).getElements();
  }

  @NotNull
  @Override
  public JpsDependenciesList getDependenciesList() {
    return myContainer.getChild(DEPENDENCIES_LIST_CHILD_ROLE);
  }

  @Override
  @NotNull
  public JpsSdkReferencesTable getSdkReferencesTable() {
    return myContainer.getChild(JpsSdkReferencesTableImpl.ROLE);
  }

  @Override
  public JpsLibraryReference getSdkReference(@NotNull JpsSdkType<?> type) {
    JpsLibraryReference sdkReference = getSdkReferencesTable().getSdkReference(type);
    if (sdkReference != null) {
      return sdkReference;
    }
    JpsProject project = getProject();
    if (project != null) {
      return project.getSdkReferencesTable().getSdkReference(type);
    }
    return null;
  }

  @Override
  public <P extends JpsSdkProperties> JpsTypedLibrary<P> getSdk(@NotNull JpsSdkType<P> type) {
    final JpsLibraryReference reference = getSdkReference(type);
    return reference != null ? (JpsTypedLibrary<P>)reference.resolve() : null;
  }

  @Override
  public void delete() {
    //noinspection unchecked
    ((JpsElementCollection<JpsModule>)myParent).removeChild(this);
  }

  @NotNull
  @Override
  public JpsModuleReference createReference() {
    return new JpsModuleReferenceImpl(getName());
  }

  @NotNull
  @Override
  public <P extends JpsElementProperties, Type extends JpsLibraryType<P> & JpsElementTypeWithDefaultProperties<P>>
  JpsLibrary addModuleLibrary(@NotNull String name, @NotNull Type type) {
    return myLibraryCollection.addLibrary(name, type);
  }

  @Override
  public void addModuleLibrary(final @NotNull JpsLibrary library) {
    myLibraryCollection.addLibrary(library);
  }

  @NotNull
  @Override
  public JpsLibraryCollection getLibraryCollection() {
    return myLibraryCollection;
  }

  @Nullable
  public JpsProject getProject() {
    JpsModel model = getModel();
    return model != null ? model.getProject() : null;
  }

  @Override
  public JpsModuleType<?> getModuleType() {
    return myContainer.getChild(TYPED_DATA_ROLE).getType();
  }
}
