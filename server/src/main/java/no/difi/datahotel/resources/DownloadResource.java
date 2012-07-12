package no.difi.datahotel.resources;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import no.difi.datahotel.logic.ChunkBean;
import no.difi.datahotel.logic.DataBean;
import no.difi.datahotel.model.Metadata;
import no.difi.datahotel.util.Formater;

@Path("/download/")
@Component
@Scope("request")
public class DownloadResource extends BaseResource {

	Logger logger = Logger.getLogger(DownloadResource.class.getSimpleName());

	@Autowired
	private DataBean dataBean;
	
	@Autowired
	private ChunkBean chunkBean;

	@GET
	@Path("{location: [a-z0-9\\-/]*}")
	public Response getFullDataset(@PathParam("location") String location) {
		Formater dataFormat = Formater.CSVCORRECT;

		Metadata metadata = dataBean.getChild(location);
		checkNotModified(metadata);
		try {
			return Response.ok(chunkBean.getFullDataset(metadata)).type(dataFormat.getMime())
					.header("ETag", metadata.getUpdated()).build();
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			return Response.ok(dataFormat.formatError(e, null)).type(dataFormat.getMime()).status(404)
					.build();
		}
	}
	
	@GET
	@Path("{location: [a-z0-9\\-/]*}/meta.xml")
	public Response getMetadata(@PathParam("location") String location) {
		Formater dataFormat = Formater.XML;

		Metadata metadata = dataBean.getChild(location);
		checkNotModified(metadata);

		try {
			return Response.ok(chunkBean.getMetadata(metadata)).type(dataFormat.getMime())
					.header("ETag", metadata.getUpdated()).build();
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			return Response.ok(dataFormat.formatError(e, null)).type(dataFormat.getMime()).status(404)
					.build();
		}
	}

	@GET
	@Path("{location: [a-z0-9\\-/]*}/fields.xml")
	public Response getFields(@PathParam("location") String location) {
		Formater dataFormat = Formater.XML;

		Metadata metadata = dataBean.getChild(location);
		checkNotModified(metadata);
		try {
			return Response.ok(chunkBean.getFields(metadata)).type(dataFormat.getMime())
					.header("ETag", metadata.getUpdated()).build();
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			return Response.ok(dataFormat.formatError(e, null)).type(dataFormat.getMime()).status(404)
					.build();
		}
	}

	public void setDataEJB(DataBean dataEJB) {
		this.dataBean = dataEJB;
	}

	public void setChunkEJB(ChunkBean chunkEJB) {
		this.chunkBean = chunkEJB;
	}
}
