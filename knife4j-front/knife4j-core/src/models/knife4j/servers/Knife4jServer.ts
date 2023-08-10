import lodash from 'lodash'
import { Knife4jServerVariableObject } from './Knife4jServerVariableObject';

/**
 * The object provides metadata about the API. The metadata can be used by the clients if needed.
 */
export class Knife4jServer {
    /**
   * A URL to the target host. This URL supports Server Variables and MAY be relative, to indicate that the host location is relative to the location where the OpenAPI document is being served.
   * Variable substitutions will be made when a variable is named in {brackets}.
   */
    url: string;

    /**
     * An optional string describing the host designated by the URL.
     * CommonMark syntax MAY be used for rich text representation.
     */
    description?: string;

    /**
     * A map between a variable name and its value.
     * The value is used for substitution in the server's URL template.
     */
    variables?: { [variable: string]: Knife4jServerVariableObject };

    constructor(url: string) {
        this.url = url
    }

    /**
     * 添加variable值
     * @param key key
     * @param value value
     */
    addVariable(key: string, value: Knife4jServerVariableObject) {
        if (lodash.isEmpty(key) || lodash.isEmpty(value)) {
            return
        }
        if (lodash.isEmpty(this.variables)) {
            this.variables = {};
        }
        this.variables[key] = value;
    }
}

