import RequestDto from '../dtos/RequestDto';
import RequestListFilter from './RequestListFilter';

export default class RequestsList {
    private requests: RequestDto[] = [];

    constructor(requests: RequestDto[]) {
        this.requests = requests;
    }

    public getRequests(filter: RequestListFilter, sortBy: { value: string, asc: boolean } = null): RequestDto[] {
        let requests = this._filter(this.requests, filter);
        return this._sortBy(requests, sortBy);
    }

    private _filter(requests: RequestDto[], filter: RequestListFilter) {
        let isSearchAlwaysValid = (!filter.search || filter.search.length < 3);
        let isProjectAlwaysValid = !filter.project;
        let isTechAlwaysValid = !filter.tech || filter.tech.length == 0;
        return requests.filter((request: RequestDto) => {
            let regex = new RegExp(filter.search, 'i');
            let valid;
            valid = filter.fase == 'all';
            valid = valid || (filter.fase == 'starred' && request.starred);
            valid = valid || filter.fase == request.fase.toLowerCase();
            valid = valid && (isProjectAlwaysValid || filter.project.toLowerCase() == request.owner.toLowerCase());
            valid = valid && (isSearchAlwaysValid || request.title.match(regex));
            valid = valid && (isTechAlwaysValid || filter.tech.every((f: string) => {
                let regex = new RegExp(`^${f}$`, 'i');
                return request.technologies.some((t: string) => regex.test(t));
            }));
            return valid;
        });
    }

    private _sortBy(requests: RequestDto[], sortBy:  { value: string, asc: boolean }) {
        if (sortBy) {
            let property = sortBy.value;
            let direction = sortBy.asc;

            return this._sortByPropertyAndDirection(requests, property, direction);
        }
        return requests;
    }

    private _sortByPropertyAndDirection(requests: RequestDto[], property: string, asc: boolean) {
        let sortFunction = this._getSortFunction(property);
        let sortedRequests = requests.sort(sortFunction);
        return this._applyDirection(sortedRequests, asc);
    }


    private _applyDirection(requests: RequestDto[], asc: boolean) {
        if (asc) {
            return requests;
        } else {
            return requests.reverse();
        }
    }

    private _getSortFunction(property) {
        let sortFunction;
        if (property.toLowerCase() == 'funding') {
            // sort fndFunds.totalAmount from high to low
            sortFunction = this._getSortByFundingFunction();
        } else {
            sortFunction = this._getSortByPropertyFunction(property);
        }
        return sortFunction;
    }

    private _getSortByFundingFunction() {
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
        };
    }

    private _getSortByPropertyFunction(sortBy: string) {
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
        };
    }
}