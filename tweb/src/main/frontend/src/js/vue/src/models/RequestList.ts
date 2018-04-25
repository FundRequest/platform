import RequestDto from '../dtos/RequestDto';
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
            valid = valid && (isTechAlwaysValid || filter.tech.every((f: string) => {
                let regex = new RegExp(`^${f}$`, 'i');
                return request.technologies.some((t: string) => regex.test(t));
            }));
            return valid;
        });
    }

    private _getFundingSortFunction() {
        return (a: RequestDto, b: RequestDto) => {
            let result = 0;
            if (a.funds.usdFunds && a.funds.usdFunds) {
                result = b.funds.usdFunds - a.funds.usdFunds;
            } else if (a.funds.usdFunds) {
                result = -1;
            } else if (b.funds.usdFunds) {
                result = 1;
            }

            return result;
        }
    }

    private _getSortByFunction(sortBy: string) {
        return (a: RequestDto, b: RequestDto) => {
            let result = 0;
            if (a.hasOwnProperty(sortBy) && b.hasOwnProperty(sortBy)) {
                let aSortBy = a[sortBy].toLowerCase();
                let bSortBy = b[sortBy].toLowerCase();

                if (aSortBy < bSortBy)
                    result = -1;
                if (aSortBy > bSortBy)
                    result = 1;
            } else if (a.hasOwnProperty(sortBy)) {
                result = 1;
            } else if (b.hasOwnProperty(sortBy)) {
                result = -1;
            }

            return result;
        }
    }

    private _sort(requests: RequestDto[], sortBy: string) {
        if (sortBy) {
            let sortFunction;
            if (sortBy.toLowerCase() == 'fundings') {
                // sort fndFunds.totalAmount from high to low
                sortFunction = this._getFundingSortFunction();
            } else {
                sortFunction = this._getSortByFunction(sortBy);
            }
            return requests.sort(sortFunction);
        } else {
            return requests;
        }
    }
}