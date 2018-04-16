import {Utils} from './utils';

class ControlButton {
    private readonly _el: HTMLElement;
    private readonly _defaultText: any;

    constructor(buttonEl, clickEvent) {
        this._el = buttonEl;
        if (this._el != null) {
            this._defaultText = this._el.innerHTML;
            this._el.addEventListener('click', (e) => clickEvent(e));
        }
    }

    public disable() {
        if (this._el != null) {
            this._el.classList.remove('d-none');
            this._el.classList.add('disabled');
        }
    }

    public enable() {
        if (this._el != null) {
            this._el.classList.remove('d-none', 'disabled');
        }
    }
}

interface Step {
    stepIcon: StepIcon;
    panel: Panel;

    setNextStep(step: Step);

    setPreviousStep(step: Step);

    setActive();

    finish();

    goToNext(): Promise<Step>;

    goToPrevious(): Promise<Step>;
}

class StepImpl implements Step {
    public stepIcon: StepIcon;
    public panel: Panel;
    private _title: string;
    private _finishedCallback: any;

    private _nextStep: Step = null;
    private _previousStep: Step = null;

    constructor(stepIcon, panel, finishedCallback = null) {
        this.stepIcon = stepIcon;
        this.panel = panel;
        this._title = stepIcon.getTitle();
        this._finishedCallback = finishedCallback;
    }

    public setNextStep(step: Step) {
        this._nextStep = step;
    }

    public setPreviousStep(step: Step) {
        this._previousStep = step;
    }

    public setActive() {
        if (this._previousStep != null) {
            this._previousStep.stepIcon.setCompleted();
            this._previousStep.panel.setBackwards();
        }
        this.stepIcon.setActive();
        this.panel.setActive();
        if (this._nextStep != null) {
            this._nextStep.stepIcon.unsetCompleted();
            this._nextStep.panel.setForwards();
        }
    }

    public finish() {
        this._finishedCallback();
    }

    public async goToNext(): Promise<Step> {
        if (await this.validate()) {
            if (this._nextStep != null) {
                this._nextStep.setActive();
                return this._nextStep;
            } else {
                this.finish();
                return this;
            }
        } else {
            return this;
        }
    }

    public async goToPrevious(): Promise<Step> {
        if (this._previousStep != null) {
            this._previousStep.setActive();
            return this._previousStep;
        } else {
            return this;
        }
    }

    protected async validate(): Promise<boolean> {
        let isValid: boolean = true;
        let panelEl = this.panel.getEl();
        let formElements: HTMLElement[] = Array.from(panelEl.querySelectorAll('[data-form-validation]'));
        for (let fieldElement of formElements) {
            let validations: string[] = fieldElement.dataset.formValidation.split(';');
            isValid = await Utils.validateHTMLElement(fieldElement, validations) && isValid;
        }

        this.panel.updateHeight();
        return isValid;
    }
}

class StepIcon {
    private readonly _el: HTMLElement;
    private readonly _title: string;

    constructor(stepEl) {
        this._el = stepEl;
        this._title = this._el.dataset.wizardStep;
    }

    public getEl() {
        return this._el;
    }

    public getTitle() {
        return this._title;
    }

    public setCompleted() {
        this._el.classList.remove('-active');
        this._el.classList.add('-completed');
    }

    public unsetCompleted() {
        this._el.classList.remove('-active', '-completed');
    }

    public setActive() {
        this._el.classList.remove('-completed');
        this._el.classList.add('-active');
    }
}

class Panel {
    private readonly _el: HTMLElement;
    private readonly _steps: Steps;

    constructor(steps: Steps, panelEl: HTMLElement) {
        this._steps = steps;
        this._el = panelEl;
        this._initButtons();
    }

    public getEl() {
        return this._el;
    }

    public setActive() {
        this._el.classList.remove('movingOutBackward', 'movingOutForward');
        this._el.classList.add('movingIn');
        this.updateHeight();
    }

    public setBackwards() {
        this._el.classList.remove('movingIn', 'movingOutForward');
        this._el.classList.add('movingOutBackward');
    }

    public setForwards() {
        this._el.classList.remove('movingIn', 'movingOutBackward');
        this._el.classList.add('movingOutForward');
    }

    public updateHeight() {
        this._el.parentElement.style.height = `${this._el.offsetHeight}px`;
    }

    private _initButtons() {
        let buttonNext = this._el.querySelector('[data-wizard-control="next"]');
        let buttonPrevious = this._el.querySelector('[data-wizard-control="previous"]');

        new ControlButton(buttonNext, (e) => {
            e.preventDefault();
            this._steps.goToNextStep();
        });

        new ControlButton(buttonPrevious, (e) => {
            e.preventDefault();
            this._steps.goToPreviousStep();
        });
    }
}

class Steps {
    private _el: HTMLFormElement;
    private readonly _stepsEl: HTMLElement[];
    private readonly _panelsEl: HTMLElement[];
    private _steps: Step[];

    public currentStep: Step;
    public numberOfSteps: number;

    constructor(wizardEl: HTMLFormElement, finishedCallback) {
        this._el = wizardEl;
        this._stepsEl = Array.from(this._el.querySelectorAll('[data-wizard-step'));
        this._panelsEl = Array.from(this._el.querySelectorAll('[data-wizard-panel]'));
        this.numberOfSteps = this._stepsEl.length;
        this._initSteps(finishedCallback);
    }

    public getEl() {
        return this._el;
    }

    public async goToNextStep() {
        this.currentStep = await this.currentStep.goToNext();
    }

    public async goToPreviousStep() {
        this.currentStep = await this.currentStep.goToPrevious();
    }

    private _setCurrentStep(step: Step) {
        this.currentStep = step;
        this.currentStep.setActive();
    }

    private _initSteps(finishedCallback) {
        this._steps = [];
        let previousStep = null;

        for (let i = 0; i < this.numberOfSteps; i++) {
            let stepIcon: StepIcon = new StepIcon(this._stepsEl[i]);
            let panel: Panel = new Panel(this, this._panelsEl[i]);
            let step: Step;

            step = new StepImpl(stepIcon, panel, i + 1 == this.numberOfSteps ? finishedCallback : null);

            if (previousStep != null) {
                previousStep.setNextStep(step);
            }

            step.setPreviousStep(previousStep);
            previousStep = step;
            this._steps.push(step);
        }

        this._setCurrentStep(this._steps[0]);
    }
}

export class Wizard {
    private readonly _el: HTMLFormElement;

    private _steps: Steps;

    constructor(wizardEl, finishedCallback) {
        this._el = wizardEl;
        this._steps = new Steps(this._el, finishedCallback);
    }
}