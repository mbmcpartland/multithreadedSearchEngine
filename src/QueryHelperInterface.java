import java.io.IOException;
import java.nio.file.Path;


public interface QueryHelperInterface {
	
	public void parseQueries(Path path, boolean exact) throws IOException;
	public void toJSON(Path path);
}
