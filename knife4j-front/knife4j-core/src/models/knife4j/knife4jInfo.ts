/**
 * The object provides metadata about the API. The metadata can be used by the clients if needed.
 */
export class Knife4jInfoObject {
    /**
     * The title of the API.
     */
    title: string;

    /**
     * A short summary of the API.
     */
    summary?: string;

    /**
     * The version of the OpenAPI document (which is distinct from the OpenAPI Specification version or the API implementation version).
     */
    version = "1.0";

    /**
     * A verbose explanation of the API. CommonMark syntax MAY be used for rich text representation.
     */
    description?: string;

    /**
     * A URL to the Terms of Service for the API. MUST be in the format of a URL.
     */
    termsOfService?: string;

    /**
     * The identifying name of the contact person/organization.
     */
    name?: string;

    /**
     * The URL pointing to the contact information. MUST be in the format of a URL.
     */
    url?: string;

    /**
     * The email address of the contact person/organization. MUST be in the format of an email address.
     */
    email?: string;

    /**
     * The license name information for the exposed API.
     */
    licenseName?: string;
    /**
     * The license url information for the exposed API.
     */
    licenseUrl?: string;

    constructor(title: string) {
        this.title = title
    }
}