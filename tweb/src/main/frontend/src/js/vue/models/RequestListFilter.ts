export default class RequestListFilter {
    search: string;
    tech: string[];
    project: string;
    fase: string;

    public get isFiltered() {
        return (!!this.search && this.search.length >= 3)
            || (!!this.tech && this.tech.length > 0)
            || (!!this.project && this.project.length > 0);
    }
}