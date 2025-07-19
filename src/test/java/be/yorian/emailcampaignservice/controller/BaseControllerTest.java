package be.yorian.emailcampaignservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

public abstract class BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc;
    @MockitoBean
    protected JpaMetamodelMappingContext metamodelMappingContext;
    @Autowired
    protected ObjectMapper objectMapper;

}
