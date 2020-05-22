package matcher;

import entity.MatchResult;

public interface IMathcher {
    public MatchResult match(String str, String keyword);
    public String buildKeyword(String str,String value);
}
