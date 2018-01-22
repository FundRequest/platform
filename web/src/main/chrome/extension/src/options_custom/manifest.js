// SAMPLE
this.manifest = {
    "name": "FundRequest",
    "icon": "icon.png",
    "settings": [
        {
            "tab": i18n.get("information"),
            "group": i18n.get("userDetails"),
            "name": "fndAccountAddress",
            "type": "text",
            "label": i18n.get("accountAddress"),
            "text": i18n.get("accountAddress-characters")
        },
        {
            "tab": i18n.get("information"),
            "group": i18n.get("userDetails"),
            "name": "fndAccountAddressDescription",
            "type": "description",
            "text": i18n.get("accountAddressDescription")
        },
        {
            "tab": i18n.get("developer"),
            "group": i18n.get("settings"),
            "name": "fndFundUrlPrefix",
            "type": "text",
            "label": "FundRequest fund url prefix",
            "text": "https://alpha.fundrequest.io/#/requests/fund?url="
        },
    ],
    "alignment": [
    ]
};
