package com.thsword.netjob.web.swagger.webscan;

import static com.google.common.collect.Maps.newTreeMap;
import io.swagger.models.Contact;
import io.swagger.models.Info;
import io.swagger.models.Model;
import io.swagger.models.Scheme;
import io.swagger.models.Swagger;
import io.swagger.models.Tag;
import io.swagger.models.parameters.Parameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiListing;
import springfox.documentation.service.Documentation;
import springfox.documentation.service.Operation;
import springfox.documentation.service.ResourceListing;
import springfox.documentation.swagger2.mappers.LicenseMapper;
import springfox.documentation.swagger2.mappers.ModelMapper;
import springfox.documentation.swagger2.mappers.ParameterMapper;
import springfox.documentation.swagger2.mappers.SecurityMapper;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2MapperImpl;
import springfox.documentation.swagger2.mappers.VendorExtensionsMapper;

import com.google.common.collect.Multimap;

/**
 * Created by yong on 2018/9/12.
 */
@Primary
@Component
public class ServiceModelToSwagger2Mapper extends ServiceModelToSwagger2MapperImpl {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ParameterMapper parameterMapper;
    @Autowired
    private SecurityMapper securityMapper;
    @Autowired
    private LicenseMapper licenseMapper;
    @Autowired
    private VendorExtensionsMapper vendorExtensionsMapper;

    Map<String, Model> modelsFromApiListings( ModelMapper modelMapper,Multimap<String, ApiListing> apiListings) {
        Map<String, springfox.documentation.schema.Model> definitions = newTreeMap();
        for (ApiListing each : apiListings.values()) {
            definitions.putAll(each.getModels());
        }
        return modelMapper.mapModels(definitions);
    }

    @Override
    public Swagger mapDocumentation(Documentation from) {
        if ( from == null ) {
            return null;
        }

        Swagger swagger = new Swagger();

        swagger.setVendorExtensions( vendorExtensionsMapper.mapExtensions( from.getVendorExtensions() ) );
        swagger.setSchemes( mapSchemes( from.getSchemes() ) );
        swagger.setPaths( mapApiListings( from.getApiListings() ) );
        swagger.setHost( from.getHost() );
        swagger.setDefinitions( modelsFromApiListings( modelMapper,from.getApiListings() ) );
        swagger.setSecurityDefinitions( securityMapper.toSecuritySchemeDefinitions( from.getResourceListing() ) );
        ApiInfo info = fromResourceListingInfo( from );
        if ( info != null ) {
            swagger.setInfo( mapApiInfo( info ) );
        }
        swagger.setBasePath( from.getBasePath() );
        swagger.setTags( tagSetToTagList( from.getTags() ) );
        List<String> list2 = from.getConsumes();
        if ( list2 != null ) {
            swagger.setConsumes( new ArrayList<String>( list2 ) );
        }
        else {
            swagger.setConsumes( null );
        }
        List<String> list3 = from.getProduces();
        if ( list3 != null ) {
            swagger.setProduces( new ArrayList<String>( list3 ) );
        }
        else {
            swagger.setProduces( null );
        }

        return swagger;
    }

    @Override
    protected Info mapApiInfo(ApiInfo from) {
        if ( from == null ) {
            return null;
        }

        Info info = new Info();

        info.setLicense( licenseMapper.apiInfoToLicense( from ) );
        info.setVendorExtensions( vendorExtensionsMapper.mapExtensions( from.getVendorExtensions() ) );
        info.setTermsOfService( from.getTermsOfServiceUrl() );
        info.setContact( map( from.getContact() ) );
        info.setDescription( from.getDescription() );
        info.setVersion( from.getVersion() );
        info.setTitle( from.getTitle() );

        return info;
    }

    @Override
    protected Contact map(springfox.documentation.service.Contact from) {
        if ( from == null ) {
            return null;
        }

        Contact contact = new Contact();

        contact.setName( from.getName() );
        contact.setUrl( from.getUrl() );
        contact.setEmail( from.getEmail() );

        return contact;
    }

//    @Override
    protected io.swagger.models.Operation mapOperation(Operation from) {
        if ( from == null ) {
            return null;
        }

        io.swagger.models.Operation operation = new io.swagger.models.Operation();

        operation.setSecurity( mapAuthorizations( from.getSecurityReferences() ) );
        operation.setVendorExtensions( vendorExtensionsMapper.mapExtensions( from.getVendorExtensions() ) );
        operation.setDescription( from.getNotes() );
        operation.setOperationId( from.getUniqueId() );
        operation.setResponses( mapResponseMessages( from.getResponseMessages() ) );
        operation.setSchemes( stringSetToSchemeList( from.getProtocol() ) );
        Set<String> set = from.getTags();
        if ( set != null ) {
            operation.setTags( new ArrayList<String>( set ) );
        }
        else {
            operation.setTags( null );
        }
        operation.setSummary( from.getSummary() );
        Set<String> set1 = from.getConsumes();
        if ( set1 != null ) {
            operation.setConsumes( new ArrayList<String>( set1 ) );
        }
        else {
            operation.setConsumes( null );
        }
        Set<String> set2 = from.getProduces();
        if ( set2 != null ) {
            operation.setProduces( new ArrayList<String>( set2 ) );
        }
        else {
            operation.setProduces( null );
        }
        operation.setParameters( parameterListToParameterList( from.getParameters() ) );
        if ( from.getDeprecated() != null ) {
            operation.setDeprecated( Boolean.parseBoolean( from.getDeprecated() ) );
        }

        return operation;
    }

    @Override
    protected Tag mapTag(springfox.documentation.service.Tag from) {
        if ( from == null ) {
            return null;
        }

        Tag tag = new Tag();

        tag.setVendorExtensions( vendorExtensionsMapper.mapExtensions( from.getVendorExtensions() ) );
        tag.setName( from.getName() );
        tag.setDescription( from.getDescription() );

        return tag;
    }

    private ApiInfo fromResourceListingInfo(Documentation documentation) {
        if ( documentation == null ) {
            return null;
        }
        ResourceListing resourceListing = documentation.getResourceListing();
        if ( resourceListing == null ) {
            return null;
        }
        ApiInfo info = resourceListing.getInfo();
        if ( info == null ) {
            return null;
        }
        return info;
    }

    protected List<Tag> tagSetToTagList(Set<springfox.documentation.service.Tag> set) {
        if ( set == null ) {
            return null;
        }

        List<Tag> list = new ArrayList<Tag>( set.size() );
        for ( springfox.documentation.service.Tag tag : set ) {
            list.add( mapTag( tag ) );
        }

        return list;
    }

    protected List<Scheme> stringSetToSchemeList(Set<String> set) {
        if ( set == null ) {
            return null;
        }

        List<Scheme> list = new ArrayList<Scheme>( set.size() );
        for ( String string : set ) {
            list.add( Enum.valueOf( Scheme.class, string ) );
        }

        return list;
    }

    protected List<Parameter> parameterListToParameterList(List<springfox.documentation.service.Parameter> list) {
        if ( list == null ) {
            return null;
        }

        List<Parameter> list1 = new ArrayList<Parameter>( list.size() );
        for ( springfox.documentation.service.Parameter parameter : list ) {
            list1.add( parameterMapper.mapParameter( parameter ) );
        }

        return list1;
    }
}