package edu.kpi.testcourse.storage;

import edu.kpi.testcourse.entities.UrlAlias;
import edu.kpi.testcourse.logic.UrlShortenerConfig;
import edu.kpi.testcourse.serialization.JsonTool;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * A file-backed implementation of {@link UrlRepository} suitable for use in production.
 */
public class UrlRepositoryFileImpl implements UrlRepository {

  // Urls, keyed by alias.
  private final Map<String, UrlAlias> urlMapByAlias;
  // All User urls, keyed by email
  private final Map<String, List<UrlAlias>> urlsMapByEmail;

  private final JsonTool jsonTool;
  private final Path jsonFilePath;

  /**
   * Creates an instance.
   */
  @Inject
  public UrlRepositoryFileImpl(JsonTool jsonTool, UrlShortenerConfig appConfig) {
    this.jsonTool = jsonTool;
  }

  @Override
  public synchronized void createUrlAlias(UrlAlias urlAlias) throws AliasAlreadyExist {
    if (urlMapByAlias.containsKey(urlAlias.alias())) {
      throw new AliasAlreadyExist();
    }

    urlMapByAlias.put(urlAlias.alias(), urlAlias);
    putInMapByEmail(urlsMapByEmail, urlAlias);

  }

  @Nullable
  @Override
  public synchronized UrlAlias findUrlAlias(String alias) {
    return urlMapByAlias.get(alias);
  }

  @Override
  public void deleteUrlAlias(String email, String alias) throws PermissionDenied {
    UrlAlias urlAlias = urlMapByAlias.get(alias);

    if (urlAlias == null) {
      throw new RuntimeException("UrlAlias record not found!");
    }

    if (!urlAlias.email().equals(email)) {
      throw new PermissionDenied();
    }

    urlMapByAlias.remove(alias);
    List<UrlAlias> userUrls = urlsMapByEmail.get(email);
    userUrls.remove(urlAlias);

  }

  @Override
  public synchronized List<UrlAlias> getAllAliasesForUser(String userEmail) {
    return urlsMapByEmail.getOrDefault(userEmail, new ArrayList<>());
  }

  private static Map<String, List<UrlAlias>> makeUrlsMapByEmail(
    Map<String, UrlAlias> urlMapByAlias
  ) {
    Map<String, List<UrlAlias>> urlsMapByEmail = new HashMap<>();

    for (UrlAlias urlAlias : urlMapByAlias.values()) {
      putInMapByEmail(urlsMapByEmail, urlAlias);
    }
    return urlsMapByEmail;
  }

  private static void putInMapByEmail(
    Map<String, List<UrlAlias>> urlsMapByEmail, UrlAlias urlAlias
  ) {
    List<UrlAlias> urlsByEmailList = urlsMapByEmail.computeIfAbsent(urlAlias.email(),
      s -> new ArrayList<>());
    urlsByEmailList.add(urlAlias);
  }



}
