/**
 * An object representing a Server Variable for server URL template substitution.
 */
export class Knife4jServerVariableObject {
    /**
     * An enumeration of string values to be used if the substitution options are from a limited set.
     */
    enum?: string[];

    /**
     * REQUIRED. The default value to use for substitution, which SHALL be sent if an alternate value is not supplied.
     * Note this behavior is different than the Schema Object's treatment of default values, because in those cases parameter values are optional.
     */
    default: string;

    /**
     * An optional description for the server variable. CommonMark syntax MAY be used for rich text representation.
     */
    description?: string;

    constructor(_def: string) {
        this.default = _def;
    }
};