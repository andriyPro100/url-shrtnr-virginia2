package edu.kpi.testcourse.storage;

import edu.kpi.testcourse.entities.UrlAlias;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Nullable;

/**
 * An in-memory fake implementation of {@link UrlRepository}.
 */
public class UrlRepositoryFakeImpl implements UrlRepository {
  private final HashMap<String, UrlAlias> aliases = new HashMap<>();

  @Override
  public void createUrlAlias(UrlAlias urlAlias) {
    if (aliases.containsKey(urlAlias.alias())) {
      throw new UrlRepository.AliasAlreadyExist();
    }

    aliases.put(urlAlias.alias(), urlAlias);
  }

  @Override
  public @Nullable UrlAlias findUrlAlias(String alias) {
    return aliases.get(alias);
  }

  @Override
  public void deleteUrlAlias(String email, String alias) {
    // TODO: We should implement it
    throw new UnsupportedOperationException();
  }

  @Override
  public List<UrlAlias> getAllAliases(String username) {
    List<UrlAlias> aliasesList = new ArrayList<>();
    for (UrlAlias urlAlias: aliases.values()) {
      if (urlAlias.email().equals(username)) {
        aliasesList.add(urlAlias);
      }
      else {
        throw new PermissionDenied();
      }
    }
    return aliasesList;
  }
}
