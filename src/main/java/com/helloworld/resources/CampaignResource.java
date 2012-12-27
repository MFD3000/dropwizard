package com.helloworld.resources;



import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.helloworld.jdbi.CampaignDAO;

import java.util.List;

@Path("/campaigns")
@Produces(MediaType.APPLICATION_JSON)
public class CampaignResource {

    private final CampaignDAO campaignDAO;

    public CampaignResource(CampaignDAO campaignDAO) {
        this.campaignDAO = campaignDAO;
    }

    

    @GET
    public List<String> listCampaigns() {
        return campaignDAO.findTerms();
    }

}
