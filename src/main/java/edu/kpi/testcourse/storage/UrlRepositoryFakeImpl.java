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
  
 /**
 * Adding of an URL alias into a HashMap storage. 
 */
@Override
public void createUrlAlias(UrlAlias urlAlias) {
  if (aliases.containsKey(urlAlias.alias())) {
    throw new UrlRepository.AliasAlreadyExist();
  }
  aliases.put(urlAlias.alias(), urlAlias);
}

 /**
 * Finding of an URL alias by alias name from the HashMap storage. 
 */ 
@Override
public @Nullable UrlAlias findUrlAlias(String alias) {
  return aliases.get(alias);
}
  
 /**
 * Deleting of an URL alias from the HashMap storage. 
 */
@Override
public void deleteUrlAlias(String email, String alias) {
  // TODO: We should implement it
  throw new UnsupportedOperationException();
}
 /**
 * Getting a list of all URL aliases of certain user. 
 */
@Override
public List<UrlAlias> getAllAliases(String username) {
  List<UrlAlias> aliasesList = new ArrayList<>();
  for (UrlAlias urlAlias : aliases.values()) {
    if (urlAlias.email().equals(username)) {
      aliasesList.add(urlAlias);
    } else {
      throw new PermissionDenied();
    }
  }
  return aliasesList;
}
}
