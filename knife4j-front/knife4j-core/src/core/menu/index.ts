class Menu {
    public name: string;

    constructor(name: string) {
        this.name = name;
    }
    sayHi() {
        return `My name - is ${this.name}`;
    }
    sayYes(userName: string) {
        const myName = this.sayHi();
        const say = `Hi:${userName},${myName}`;
        return say;
    }
}


export default Menu;