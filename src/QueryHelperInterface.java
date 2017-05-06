import java.io.IOException;
import java.nio.file.Path;

// TODO Javadoc here, @Override and skip the Javadoc in your other classes

public interface QueryHelperInterface {
	
	public void parseQueries(Path path, boolean exact) throws IOException;
	public void toJSON(Path path);
}
