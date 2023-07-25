/**
 * External Documentation Object
 */
export class Knife4jExternalDocumentationObject {
    /**
     * URL for the external documentation.
     */
    url: string;
  
    /**
     * Brief description of the external documentation.
     */
    description?: string;
    
    constructor(url:string){
        this.url=url
    }
    
  }