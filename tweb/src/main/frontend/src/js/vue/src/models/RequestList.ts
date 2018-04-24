import RequestDto from '../dtos/RequestDto';
import {Utils} from '../../../app/Utils';
import RequestListFilter from './RequestListFilter';

export default class RequestsList {
    private requests: RequestDto[] = [];

    constructor(requests: RequestDto[]) {
        this.requests = requests;
    }

    public getRequests(filter: RequestListFilter, sortBy: string = null): RequestDto[] {
        let requests = this._filter(this.requests, filter);
        return this._sort(requests, sortBy);
    }

    private _filter(requests: RequestDto[], filter: RequestListFilter) {
        let isSearchAlwaysValid = (!filter.search || filter.search.length < 3);
        let isProjectAlwaysValid = !filter.project;
        let isTechAlwaysValid = !filter.tech || filter.tech.length == 0;
        return requests.filter((request: RequestDto) => {
            let regex = new RegExp(filter.search, 'i');
            let valid;
            valid = filter.status == 'all';
            valid = valid || (filter.status == 'starred' && request.starred);
            valid = valid || filter.status == request.status.toLowerCase();
            valid = valid && (isProjectAlwaysValid || filter.project.toLowerCase() == request.owner.toLowerCase());
            valid = valid && (isSearchAlwaysValid || request.title.match(regex));
            valid = valid && (isTechAlwaysValid || filter.tech.some((f: string) => {
                return request.technologies.some((t: string) => new RegExp(t, 'i').test(f));
            }));
            return valid;
        });
    }

    private _sort(requests: RequestDto[], sortBy: string) {
        if (sortBy) {
            let sortFunction;
            if (sortBy.toLowerCase() == 'fundings') {
                // sort fndFunds.totalAmount from high to low
                sortFunction = (a: RequestDto, b: RequestDto) => {
                    if (a.funds.usdFunds && a.funds.usdFunds) {
                        return b.funds.usdFunds - a.funds.usdFunds;
                    } else if (a.funds.usdFunds) {
                        return -1;
                    } else if (b.funds.usdFunds) {
                        return 1;
                    }

                    return 0;
                };
            } else {
                sortFunction = (a: RequestDto, b: RequestDto) => {
                    if (a.hasOwnProperty(sortBy) && b.hasOwnProperty(sortBy)) {
                        let aSortBy = a[sortBy].toLowerCase();
                        let bSortBy = b[sortBy].toLowerCase();

                        if (aSortBy < bSortBy)
                            return -1;
                        if (aSortBy > bSortBy)
                            return 1;
                    } else if (a.hasOwnProperty(sortBy)) {
                        return 1;
                    } else if (b.hasOwnProperty(sortBy)) {
                        return -1;
                    }

                    return 0;
                };
            }
            return requests.sort(sortFunction);
        } else {
            return requests;
        }
    }
}