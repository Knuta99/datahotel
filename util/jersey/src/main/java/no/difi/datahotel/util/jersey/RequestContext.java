package no.difi.datahotel.util.jersey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import no.difi.datahotel.util.bridge.Field;
import no.difi.datahotel.util.bridge.Metadata;

public class RequestContext {

	private int page = 1;
	private String query = null;
	private Map<String, String> lookup = new HashMap<String, String>();
	private String callback;
	private List<Field> fields;
	private Metadata metadata;

	public RequestContext() {

	}

	public RequestContext(UriInfo uriInfo) {
		this(uriInfo, null, null);
	}

	public RequestContext(UriInfo uriInfo, Metadata metadata, List<Field> fields) {
		this.fields = fields;
		this.metadata = metadata;

		MultivaluedMap<String, String> parameters = uriInfo.getQueryParameters();

		if (fields != null)
			for (Field f : fields)
				if (f.getGroupable())
					if (parameters.containsKey(f.getShortName()))
						lookup.put(f.getShortName(), parameters.getFirst(f.getShortName()));

		if (parameters.containsKey("query"))
			query = parameters.getFirst("query");

		if (parameters.containsKey("page"))
			page = Integer.parseInt(parameters.getFirst("page"));
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Map<String, String> getLookup() {
		return lookup;
	}

	public void setLookup(Map<String, String> lookup) {
		this.lookup = lookup;
	}

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public boolean isSearch() {
		return query != null || lookup.size() > 0;
	}
}
