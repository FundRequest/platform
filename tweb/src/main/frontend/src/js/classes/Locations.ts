export class Locations {
    public static requests = '/requests';
    public static requestDetail = '/requests/{id}';
    public static requestsUser = '/user/requests';
    public static help = 'https://help.fundrequest.io';

    public static getRequestDetailUrl(id: string | number) {
        return this.requestDetail.replace('{id}', `${id}`)
    }

    public static gotoRequestDetail(id: string | number) {
        this._goto(Locations.getRequestDetailUrl(id));
    }

    public static getHelpPageUrl(id: string) {
        return `${this.help}/${id}`;
    }

    private static _goto(url) {
        window.location.href = url;
    }
}