package com.sarapis.orservice.utils;

import com.sarapis.orservice.model.*;

import java.util.Map;
import java.util.function.BiConsumer;

import static com.sarapis.orservice.utils.Parser.*;

public class FieldMap {

    public static final Map<String, BiConsumer<Organization, String>> ORGANIZATION_FIELD_MAP = Map.ofEntries(
            Map.entry("name", Organization::setName),
            Map.entry("alternateName", Organization::setAlternateName),
            Map.entry("description", Organization::setDescription),
            Map.entry("email", Organization::setEmail),
            Map.entry("website", Organization::setWebsite),
            Map.entry("taxStatus", Organization::setTaxStatus),
            Map.entry("taxId", Organization::setTaxId),
            Map.entry("yearIncorporated", parseIntegerAndSet(Organization::setYearIncorporated)),
            Map.entry("legalStatus", Organization::setLegalStatus),
            Map.entry("logo", Organization::setLogo),
            Map.entry("uri", Organization::setUri),
            Map.entry("services", parseObjectAndSet(Organization::setServices)),
            Map.entry("additionalWebsites", parseObjectAndSet(Organization::setAdditionalWebsites)),
            Map.entry("funding", parseObjectAndSet(Organization::setFunding)),
            Map.entry("contacts", parseObjectAndSet(Organization::setContacts)),
            Map.entry("phones", parseObjectAndSet(Organization::setPhones)),
            Map.entry("programs", parseObjectAndSet(Organization::setPrograms)),
            Map.entry("organizationIdentifiers", parseObjectAndSet(Organization::setOrganizationIdentifiers)),
            Map.entry("locations", parseObjectAndSet(Organization::setLocations))
    );

    public static final Map<String, BiConsumer<ServiceAtLocation, String>> SERVICE_AT_LOCATION_FIELD_MAP = Map.ofEntries(
            Map.entry("service", parseObjectAndSet(ServiceAtLocation::setService)),
            Map.entry("location", parseObjectAndSet(ServiceAtLocation::setLocation)),
            Map.entry("description", ServiceAtLocation::setDescription),
            Map.entry("contacts", parseObjectAndSet(ServiceAtLocation::setContacts)),
            Map.entry("phones", parseObjectAndSet(ServiceAtLocation::setPhones)),
            Map.entry("schedules", parseObjectAndSet(ServiceAtLocation::setSchedules)),
            Map.entry("serviceAreas", parseObjectAndSet(ServiceAtLocation::setServiceAreas))
    );

    public static final Map<String, BiConsumer<Service, String>> SERVICE_FIELD_MAP = Map.ofEntries(
            Map.entry("organization", parseObjectAndSet(Service::setOrganization)),
            Map.entry("name", Service::setName),
            Map.entry("alternateName", Service::setAlternateName),
            Map.entry("description", Service::setDescription),
            Map.entry("url", Service::setUrl),
            Map.entry("email", Service::setEmail),
            Map.entry("status", Service::setStatus),
            Map.entry("interpretationServices", Service::setInterpretationServices),
            Map.entry("feesDescription", Service::setFeesDescription),
            Map.entry("waitTime", Service::setWaitTime),
            Map.entry("fees", Service::setFees),
            Map.entry("accreditations", Service::setAccreditations),
            Map.entry("eligibility_description", Service::setEligibility_description),   /* ? */
            Map.entry("minimumAge", parseIntegerAndSet(Service::setMinimumAge)),
            Map.entry("maximumAge", parseIntegerAndSet(Service::setMaximumAge)),
            Map.entry("assuredDate", Service::setAssuredDate),
            Map.entry("assurerEmail", Service::setAssurerEmail),
            Map.entry("licenses", Service::setLicenses),
            Map.entry("alert", Service::setAlert),
            Map.entry("lastModified", parseDateAndSet(Service::setLastModified)),
            Map.entry("phones", parseObjectAndSet(Service::setPhones)),
            Map.entry("schedules", parseObjectAndSet(Service::setSchedules)),
            Map.entry("serviceAreas", parseObjectAndSet(Service::setServiceAreas)),
            Map.entry("serviceAtLocations", parseObjectAndSet(Service::setServiceAtLocations)),
            Map.entry("languages", parseObjectAndSet(Service::setLanguages)),
            Map.entry("funding", parseObjectAndSet(Service::setFunding)),
            Map.entry("costOptions", parseObjectAndSet(Service::setCostOptions)),
            Map.entry("requiredDocuments", parseObjectAndSet(Service::setRequiredDocuments)),
            Map.entry("contacts", parseObjectAndSet(Service::setContacts)),
            Map.entry("additionalUrls", parseObjectAndSet(Service::setAdditionalUrls))
    );

    public static final Map<String, BiConsumer<Taxonomy, String>> TAXONOMY_FIELD_MAP = Map.ofEntries(
            Map.entry("name", Taxonomy::setName),
            Map.entry("description", Taxonomy::setDescription),
            Map.entry("version", Taxonomy::setVersion),
            Map.entry("uri", Taxonomy::setUri)
    );

    public static final Map<String, BiConsumer<TaxonomyTerm, String>> TAXONOMY_TERM_FIELD_MAP = Map.ofEntries(
            Map.entry("code", TaxonomyTerm::setCode),
            Map.entry("name", TaxonomyTerm::setName),
            Map.entry("description", TaxonomyTerm::setDescription),
            Map.entry("parent", parseObjectAndSet(TaxonomyTerm::setParent)),
            Map.entry("taxonomy", TaxonomyTerm::setTaxonomy),
            Map.entry("taxonomyDetail", parseObjectAndSet(TaxonomyTerm::setTaxonomyDetail)),
            Map.entry("language", TaxonomyTerm::setLanguage),
            Map.entry("termUri", TaxonomyTerm::setTermUri)
    );

    public static final Map<String, BiConsumer<Location, String>> LOCATION_FIELD_MAP = Map.ofEntries(
            Map.entry("locationType", parseEnumAndSet(Location::setLocationType, LocationType.class)),
            Map.entry("url", Location::setUrl),
            Map.entry("organization", parseObjectAndSet(Location::setOrganization)),
            Map.entry("name", Location::setName),
            Map.entry("alternateName", Location::setAlternateName),
            Map.entry("description", Location::setDescription),
            Map.entry("transportation", Location::setTransportation),
            Map.entry("latitude", parseIntegerAndSet(Location::setLatitude)),
            Map.entry("longitude", parseIntegerAndSet(Location::setLongitude)),
            Map.entry("externalIdentifier", Location::setExternalIdentifier),
            Map.entry("externalIdentifierType", Location::setExternalIdentifierType),
            Map.entry("languages", parseObjectAndSet(Location::setLanguages)),
            Map.entry("addresses", parseObjectAndSet(Location::setAddresses)),
            Map.entry("contacts", parseObjectAndSet(Location::setContacts)),
            Map.entry("accessibility", parseObjectAndSet(Location::setAccessibility)),
            Map.entry("phones", parseObjectAndSet(Location::setPhones)),
            Map.entry("schedules", parseObjectAndSet(Location::setSchedules))
    );
}
