export default class RequestListFilter {
    search: string;
    tech: string[];
    project: string;
    status: string;

    public get isFiltered() {
        return this.search.length > 3 || this.tech.length > 0 || this.project.length > 0;
    }
}