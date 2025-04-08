import{_ as P}from"./v-AUqG7Q.js";import{c as V,s as $}from"./EsrfRR3E.js";import{a0 as z,o as g,c as f,a as i,F as C,A,b as d,P as x,I as _,B as I,ag as T,L as N,M as q,z as L,N as R,y as h,t as v,ah as k,ai as B,Y as M,aj as U,w as p,r as w,C as m,D as H,E as W,d as Y}from"./CgXyU8S8.js";import{u as X}from"./Dkxuxu-Q.js";import{s as G,a as J}from"./BKMwx7YX.js";import{R as D}from"./OtOWOkM3.js";import{u as Q}from"./B0M2IW21.js";import"./BfvwIcQv.js";import"./bYOJpIkd.js";const Z={key:0,class:"flex bg-amber-100/70 py-2 px-3 my-4 rounded border-amber-400 border-y border-r border-l-4 text-sm"},tt={class:"my-2"},et=z({__name:"warning",props:["services"],setup(e){const n=e;return(t,l)=>{const s=P,o=V;return n.services?(g(),f("section",Z,[l[2]||(l[2]=i("i",{class:"pi pi-exclamation-triangle mt-[2px] mr-2"},null,-1)),i("div",null,[l[0]||(l[0]=i("h2",{class:"font-bold mb-1"},"Деякі послуги не назначені до жодного типу заходу.",-1)),l[1]||(l[1]=i("div",null," Якщо ви не зазначите послугу до типу заходу, ви не зможете додавати її до графіку ",-1)),i("div",tt,[(g(!0),f(C,null,A(e.services,r=>(g(),_(s,{value:r},null,8,["value"]))),256))]),d(o,{size:"small",severity:"contrast",variant:"outlined",label:"Назначити всі",class:"!text-xs my-1 !bg-white"})])])):x("",!0)}}});var nt=function(n){var t=n.dt;return`
.p-togglebutton {
    display: inline-flex;
    cursor: pointer;
    user-select: none;
    align-items: center;
    justify-content: center;
    overflow: hidden;
    position: relative;
    color: `.concat(t("togglebutton.color"),`;
    background: `).concat(t("togglebutton.background"),`;
    border: 1px solid `).concat(t("togglebutton.border.color"),`;
    padding: `).concat(t("togglebutton.padding"),`;
    font-size: 1rem;
    font-family: inherit;
    font-feature-settings: inherit;
    transition: background `).concat(t("togglebutton.transition.duration"),", color ").concat(t("togglebutton.transition.duration"),", border-color ").concat(t("togglebutton.transition.duration"),`,
        outline-color `).concat(t("togglebutton.transition.duration"),", box-shadow ").concat(t("togglebutton.transition.duration"),`;
    border-radius: `).concat(t("togglebutton.border.radius"),`;
    outline-color: transparent;
    font-weight: `).concat(t("togglebutton.font.weight"),`;
}

.p-togglebutton-content {
    position: relative;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    gap: `).concat(t("togglebutton.gap"),`;
}

.p-togglebutton-label,
.p-togglebutton-icon {
    position: relative;
    transition: none;
}

.p-togglebutton::before {
    content: "";
    background: transparent;
    transition: background `).concat(t("togglebutton.transition.duration"),", color ").concat(t("togglebutton.transition.duration"),", border-color ").concat(t("togglebutton.transition.duration"),`,
            outline-color `).concat(t("togglebutton.transition.duration"),", box-shadow ").concat(t("togglebutton.transition.duration"),`;
    position: absolute;
    inset-inline-start: `).concat(t("togglebutton.content.left"),`;
    inset-block-start: `).concat(t("togglebutton.content.top"),`;
    width: calc(100% - calc(2 * `).concat(t("togglebutton.content.left"),`));
    height: calc(100% - calc(2 * `).concat(t("togglebutton.content.top"),`));
    border-radius: `).concat(t("togglebutton.border.radius"),`;
}

.p-togglebutton.p-togglebutton-checked::before {
    background: `).concat(t("togglebutton.content.checked.background"),`;
    box-shadow: `).concat(t("togglebutton.content.checked.shadow"),`;
}

.p-togglebutton:not(:disabled):not(.p-togglebutton-checked):hover {
    background: `).concat(t("togglebutton.hover.background"),`;
    color: `).concat(t("togglebutton.hover.color"),`;
}

.p-togglebutton.p-togglebutton-checked {
    background: `).concat(t("togglebutton.checked.background"),`;
    border-color: `).concat(t("togglebutton.checked.border.color"),`;
    color: `).concat(t("togglebutton.checked.color"),`;
}

.p-togglebutton:focus-visible {
    box-shadow: `).concat(t("togglebutton.focus.ring.shadow"),`;
    outline: `).concat(t("togglebutton.focus.ring.width")," ").concat(t("togglebutton.focus.ring.style")," ").concat(t("togglebutton.focus.ring.color"),`;
    outline-offset: `).concat(t("togglebutton.focus.ring.offset"),`;
}

.p-togglebutton.p-invalid {
    border-color: `).concat(t("togglebutton.invalid.border.color"),`;
}

.p-togglebutton:disabled {
    opacity: 1;
    cursor: default;
    background: `).concat(t("togglebutton.disabled.background"),`;
    border-color: `).concat(t("togglebutton.disabled.border.color"),`;
    color: `).concat(t("togglebutton.disabled.color"),`;
}

.p-togglebutton-icon {
    color: `).concat(t("togglebutton.icon.color"),`;
}

.p-togglebutton:not(:disabled):not(.p-togglebutton-checked):hover .p-togglebutton-icon {
    color: `).concat(t("togglebutton.icon.hover.color"),`;
}

.p-togglebutton.p-togglebutton-checked .p-togglebutton-icon {
    color: `).concat(t("togglebutton.icon.checked.color"),`;
}

.p-togglebutton:disabled .p-togglebutton-icon {
    color: `).concat(t("togglebutton.icon.disabled.color"),`;
}

.p-togglebutton-sm {
    padding: `).concat(t("togglebutton.sm.padding"),`;
    font-size: `).concat(t("togglebutton.sm.font.size"),`;
}

.p-togglebutton-lg {
    padding: `).concat(t("togglebutton.lg.padding"),`;
    font-size: `).concat(t("togglebutton.lg.font.size"),`;
}
`)},ot={root:function(n){var t=n.instance,l=n.props;return["p-togglebutton p-component",{"p-togglebutton-checked":t.active,"p-invalid":t.$invalid,"p-togglebutton-sm p-inputfield-sm":l.size==="small","p-togglebutton-lg p-inputfield-lg":l.size==="large"}]},content:"p-togglebutton-content",icon:"p-togglebutton-icon",label:"p-togglebutton-label"},lt=I.extend({name:"togglebutton",theme:nt,classes:ot}),at={name:"BaseToggleButton",extends:$,props:{onIcon:String,offIcon:String,onLabel:{type:String,default:"Yes"},offLabel:{type:String,default:"No"},iconPos:{type:String,default:"left"},readonly:{type:Boolean,default:!1},tabindex:{type:Number,default:null},ariaLabelledby:{type:String,default:null},ariaLabel:{type:String,default:null},size:{type:String,default:null}},style:lt,provide:function(){return{$pcToggleButton:this,$parentInstance:this}}},K={name:"ToggleButton",extends:at,inheritAttrs:!1,emits:["change"],methods:{getPTOptions:function(n){var t=n==="root"?this.ptmi:this.ptm;return t(n,{context:{active:this.active,disabled:this.disabled}})},onChange:function(n){!this.disabled&&!this.readonly&&(this.writeValue(!this.d_value,n),this.$emit("change",n))},onBlur:function(n){var t,l;(t=(l=this.formField).onBlur)===null||t===void 0||t.call(l,n)}},computed:{active:function(){return this.d_value===!0},hasLabel:function(){return T(this.onLabel)&&T(this.offLabel)},label:function(){return this.hasLabel?this.d_value?this.onLabel:this.offLabel:"&nbsp;"}},directives:{ripple:D}},rt=["tabindex","disabled","aria-pressed","aria-labelledby","data-p-checked","data-p-disabled"];function it(e,n,t,l,s,o){var r=N("ripple");return q((g(),f("button",h({type:"button",class:e.cx("root"),tabindex:e.tabindex,disabled:e.disabled,"aria-pressed":e.d_value,onClick:n[0]||(n[0]=function(){return o.onChange&&o.onChange.apply(o,arguments)}),onBlur:n[1]||(n[1]=function(){return o.onBlur&&o.onBlur.apply(o,arguments)})},o.getPTOptions("root"),{"aria-labelledby":e.ariaLabelledby,"data-p-checked":o.active,"data-p-disabled":e.disabled}),[i("span",h({class:e.cx("content")},o.getPTOptions("content")),[L(e.$slots,"default",{},function(){return[L(e.$slots,"icon",{value:e.d_value,class:R(e.cx("icon"))},function(){return[e.onIcon||e.offIcon?(g(),f("span",h({key:0,class:[e.cx("icon"),e.d_value?e.onIcon:e.offIcon]},o.getPTOptions("icon")),null,16)):x("",!0)]}),i("span",h({class:e.cx("label")},o.getPTOptions("label")),v(o.label),17)]})],16)],16,rt)),[[r]])}K.render=it;var st=function(n){var t=n.dt;return`
.p-selectbutton {
    display: inline-flex;
    user-select: none;
    vertical-align: bottom;
    outline-color: transparent;
    border-radius: `.concat(t("selectbutton.border.radius"),`;
}

.p-selectbutton .p-togglebutton {
    border-radius: 0;
    border-width: 1px 1px 1px 0;
}

.p-selectbutton .p-togglebutton:focus-visible {
    position: relative;
    z-index: 1;
}

.p-selectbutton .p-togglebutton:first-child {
    border-inline-start-width: 1px;
    border-start-start-radius: `).concat(t("selectbutton.border.radius"),`;
    border-end-start-radius: `).concat(t("selectbutton.border.radius"),`;
}

.p-selectbutton .p-togglebutton:last-child {
    border-start-end-radius: `).concat(t("selectbutton.border.radius"),`;
    border-end-end-radius: `).concat(t("selectbutton.border.radius"),`;
}

.p-selectbutton.p-invalid {
    outline: 1px solid `).concat(t("selectbutton.invalid.border.color"),`;
    outline-offset: 0;
}
`)},ut={root:function(n){var t=n.instance;return["p-selectbutton p-component",{"p-invalid":t.$invalid}]}},ct=I.extend({name:"selectbutton",theme:st,classes:ut}),dt={name:"BaseSelectButton",extends:$,props:{options:Array,optionLabel:null,optionValue:null,optionDisabled:null,multiple:Boolean,allowEmpty:{type:Boolean,default:!0},dataKey:null,ariaLabelledby:{type:String,default:null},size:{type:String,default:null}},style:ct,provide:function(){return{$pcSelectButton:this,$parentInstance:this}}};function gt(e,n){var t=typeof Symbol<"u"&&e[Symbol.iterator]||e["@@iterator"];if(!t){if(Array.isArray(e)||(t=F(e))||n){t&&(e=t);var l=0,s=function(){};return{s,n:function(){return l>=e.length?{done:!0}:{done:!1,value:e[l++]}},e:function(c){throw c},f:s}}throw new TypeError(`Invalid attempt to iterate non-iterable instance.
In order to be iterable, non-array objects must have a [Symbol.iterator]() method.`)}var o,r=!0,a=!1;return{s:function(){t=t.call(e)},n:function(){var c=t.next();return r=c.done,c},e:function(c){a=!0,o=c},f:function(){try{r||t.return==null||t.return()}finally{if(a)throw o}}}}function bt(e){return mt(e)||ft(e)||F(e)||pt()}function pt(){throw new TypeError(`Invalid attempt to spread non-iterable instance.
In order to be iterable, non-array objects must have a [Symbol.iterator]() method.`)}function F(e,n){if(e){if(typeof e=="string")return O(e,n);var t={}.toString.call(e).slice(8,-1);return t==="Object"&&e.constructor&&(t=e.constructor.name),t==="Map"||t==="Set"?Array.from(e):t==="Arguments"||/^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(t)?O(e,n):void 0}}function ft(e){if(typeof Symbol<"u"&&e[Symbol.iterator]!=null||e["@@iterator"]!=null)return Array.from(e)}function mt(e){if(Array.isArray(e))return O(e)}function O(e,n){(n==null||n>e.length)&&(n=e.length);for(var t=0,l=Array(n);t<n;t++)l[t]=e[t];return l}var j={name:"SelectButton",extends:dt,inheritAttrs:!1,emits:["change"],methods:{getOptionLabel:function(n){return this.optionLabel?k(n,this.optionLabel):n},getOptionValue:function(n){return this.optionValue?k(n,this.optionValue):n},getOptionRenderKey:function(n){return this.dataKey?k(n,this.dataKey):this.getOptionLabel(n)},isOptionDisabled:function(n){return this.optionDisabled?k(n,this.optionDisabled):!1},onOptionSelect:function(n,t,l){var s=this;if(!(this.disabled||this.isOptionDisabled(t))){var o=this.isSelected(t);if(!(o&&!this.allowEmpty)){var r=this.getOptionValue(t),a;this.multiple?o?a=this.d_value.filter(function(u){return!B(u,r,s.equalityKey)}):a=this.d_value?[].concat(bt(this.d_value),[r]):[r]:a=o?null:r,this.writeValue(a,n),this.$emit("change",{event:n,value:a})}}},isSelected:function(n){var t=!1,l=this.getOptionValue(n);if(this.multiple){if(this.d_value){var s=gt(this.d_value),o;try{for(s.s();!(o=s.n()).done;){var r=o.value;if(B(r,l,this.equalityKey)){t=!0;break}}}catch(a){s.e(a)}finally{s.f()}}}else t=B(this.d_value,l,this.equalityKey);return t}},computed:{equalityKey:function(){return this.optionValue?null:this.dataKey}},directives:{ripple:D},components:{ToggleButton:K}},ht=["aria-labelledby"];function vt(e,n,t,l,s,o){var r=M("ToggleButton");return g(),f("div",h({class:e.cx("root"),role:"group","aria-labelledby":e.ariaLabelledby},e.ptmi("root")),[(g(!0),f(C,null,A(e.options,function(a,u){return g(),_(r,{key:o.getOptionRenderKey(a),modelValue:o.isSelected(a),onLabel:o.getOptionLabel(a),offLabel:o.getOptionLabel(a),disabled:e.disabled||o.isOptionDisabled(a),unstyled:e.unstyled,size:e.size,onChange:function(S){return o.onOptionSelect(S,a,u)},pt:e.ptm("pcToggleButton")},U({_:2},[e.$slots.option?{name:"default",fn:p(function(){return[L(e.$slots,"option",{option:a,index:u},function(){return[i("span",h({ref_for:!0},e.ptm("pcToggleButton").label),v(o.getOptionLabel(a)),17)]})]}),key:"0"}:void 0]),1032,["modelValue","onLabel","offLabel","disabled","unstyled","size","onChange","pt"])}),128))],16,ht)}j.render=vt;const yt={class:"mx-4 my-6 w-full relative"},kt={class:"flex justify-between"},St={class:"mt-6"},Bt={class:"flex items-center"},Ct=z({__name:"index",setup(e){const{data:n,status:t}=Q("/api/services","$3XDgiUTjVo"),l=w(1),s=w([{name:"Разове відвідування",value:1},{name:"Абонемент",value:2}]),o=w(!1);return(r,a)=>{const u=j,c=V,S=et,y=J,E=G;return g(),f("main",yt,[i("section",kt,[a[1]||(a[1]=i("h1",{class:"text-lg font-bold"},"Послуги",-1)),d(u,{modelValue:m(l),"onUpdate:modelValue":a[0]||(a[0]=b=>H(l)?l.value=b:null),options:m(s),"option-value":"value","option-label":"name",size:"small"},null,8,["modelValue","options"]),d(c,{type:"submit",size:"small",label:"Новий тип заходу",class:"before:content-['+'] before:block before:text-lg before:leading-[0] before:pb-[2px]"})]),m(o)?(g(),_(S,{key:0,services:m(n)},null,8,["services"])):x("",!0),i("section",St,[d(E,{value:m(n),dataKey:"id",tableStyle:"min-width: 50rem"},{footer:p(()=>[d(c,{label:"Нова послуга",size:"small",severity:"secondary",variant:"text",icon:"pi pi-plus",onClick:()=>{}})]),default:p(()=>[d(y,{field:"name",header:"Послуга"},{body:p(({data:b})=>[i("div",Bt,[i("div",{class:"w-5 h-5 mx-2 rounded",style:W({backgroundColor:b.color})},null,4),i("div",null,v(b.name),1)])]),_:1}),d(y,{field:"duration",header:"Тривалість"},{body:p(({data:b})=>[i("div",null,v(("useFormatMinutes"in r?r.useFormatMinutes:m(X))(b.duration)),1)]),_:1}),d(y,{field:"room",header:"Ціна",headerStyle:"display:flex;justify-content:end",bodyStyle:"text-align:right"},{body:p(({data:b})=>[i("span",null,v(b.price||0),1),a[2]||(a[2]=Y("   ")),a[3]||(a[3]=i("span",null,"грн.",-1))]),_:1}),d(y,null,{body:p(({data:b})=>[d(c,{icon:"pi pi-trash",severity:"danger",size:"small",variant:"text","aria-label":"Trash",class:"h-full",onOnClick:()=>{}})]),_:1})]),_:1},8,["value"])])])}}});export{Ct as default};
