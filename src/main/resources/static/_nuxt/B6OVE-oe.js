import{n as E,p as C,q as V,s as S,u as z,r as f,v as T,x as L,m as N,B as A,o as b,c as w,a as n,y as x,z as H,F as y,A as F,t as v,b as m,C as h,D as $,E as D,G as I,H as U,g as O,I as M,w as B,d as _,_ as R}from"./CgXyU8S8.js";import{s as W,a as q,b as G,c as J}from"./EsrfRR3E.js";import{s as K}from"./BgU0ikQd.js";import{u as Q}from"./B0M2IW21.js";import"./OtOWOkM3.js";const P=new WeakMap;function X(l){if(P.has(l))return P.get(l);const t={...l};return t.render?t.render=(e,d,u,s,r,o)=>{var a;if(s.mounted$??e.mounted$){const i=(a=l.render)==null?void 0:a.bind(e)(e,d,u,s,r,o);return i.children===null||typeof i.children=="string"?E(i):C(i)}else{const i=V(e._.vnode.el??null)??["<div></div>"];return S(i.join(""),i.length)}}:t.template&&(t.template=`
      <template v-if="mounted$">${l.template}</template>
      <template v-else><div></div></template>
    `),t.setup=(e,d)=>{var a;const u=z(),s=f(u.isHydrating===!1),r=N();if(u.isHydrating){const i={...r.attrs},g=Y(r);for(const p in i)delete r.attrs[p];T(()=>{Object.assign(r.attrs,i),r.vnode.dirs=g})}T(()=>{s.value=!0});const o=((a=l.setup)==null?void 0:a.call(l,e,d))||{};return L(o)?Promise.resolve(o).then(i=>typeof i!="function"?(i=i||{},i.mounted$=s,i):(...g)=>{if(s.value||!u.isHydrating){const p=i(...g);return p.children===null||typeof p.children=="string"?E(p):C(p)}else{const p=V((r==null?void 0:r.vnode.el)??null)??["<div></div>"];return S(p.join(""),p.length)}}):typeof o=="function"?(...i)=>{if(s.value)return C(o(...i),d.attrs);const g=V((r==null?void 0:r.vnode.el)??null)??["<div></div>"];return S(g.join(""),g.length)}:Object.assign(o,{mounted$:s})},P.set(l,t),t}function Y(l){if(!l||!l.vnode.dirs)return null;const t=l.vnode.dirs;return l.vnode.dirs=null,t}var Z=function(t){var e=t.dt;return`
.p-toggleswitch {
    display: inline-block;
    width: `.concat(e("toggleswitch.width"),`;
    height: `).concat(e("toggleswitch.height"),`;
}

.p-toggleswitch-input {
    cursor: pointer;
    appearance: none;
    position: absolute;
    top: 0;
    inset-inline-start: 0;
    width: 100%;
    height: 100%;
    padding: 0;
    margin: 0;
    opacity: 0;
    z-index: 1;
    outline: 0 none;
    border-radius: `).concat(e("toggleswitch.border.radius"),`;
}

.p-toggleswitch-slider {
    display: inline-block;
    cursor: pointer;
    width: 100%;
    height: 100%;
    border-width: `).concat(e("toggleswitch.border.width"),`;
    border-style: solid;
    border-color: `).concat(e("toggleswitch.border.color"),`;
    background: `).concat(e("toggleswitch.background"),`;
    transition: background `).concat(e("toggleswitch.transition.duration"),", color ").concat(e("toggleswitch.transition.duration"),", border-color ").concat(e("toggleswitch.transition.duration"),", outline-color ").concat(e("toggleswitch.transition.duration"),", box-shadow ").concat(e("toggleswitch.transition.duration"),`;
    border-radius: `).concat(e("toggleswitch.border.radius"),`;
    outline-color: transparent;
    box-shadow: `).concat(e("toggleswitch.shadow"),`;
}

.p-toggleswitch-handle {
    position: absolute;
    top: 50%;
    display: flex;
    justify-content: center;
    align-items: center;
    background: `).concat(e("toggleswitch.handle.background"),`;
    color: `).concat(e("toggleswitch.handle.color"),`;
    width: `).concat(e("toggleswitch.handle.size"),`;
    height: `).concat(e("toggleswitch.handle.size"),`;
    inset-inline-start: `).concat(e("toggleswitch.gap"),`;
    margin-block-start: calc(-1 * calc(`).concat(e("toggleswitch.handle.size"),` / 2));
    border-radius: `).concat(e("toggleswitch.handle.border.radius"),`;
    transition: background `).concat(e("toggleswitch.transition.duration"),", color ").concat(e("toggleswitch.transition.duration"),", inset-inline-start ").concat(e("toggleswitch.slide.duration"),", box-shadow ").concat(e("toggleswitch.slide.duration"),`;
}

.p-toggleswitch.p-toggleswitch-checked .p-toggleswitch-slider {
    background: `).concat(e("toggleswitch.checked.background"),`;
    border-color: `).concat(e("toggleswitch.checked.border.color"),`;
}

.p-toggleswitch.p-toggleswitch-checked .p-toggleswitch-handle {
    background: `).concat(e("toggleswitch.handle.checked.background"),`;
    color: `).concat(e("toggleswitch.handle.checked.color"),`;
    inset-inline-start: calc(`).concat(e("toggleswitch.width")," - calc(").concat(e("toggleswitch.handle.size")," + ").concat(e("toggleswitch.gap"),`));
}

.p-toggleswitch:not(.p-disabled):has(.p-toggleswitch-input:hover) .p-toggleswitch-slider {
    background: `).concat(e("toggleswitch.hover.background"),`;
    border-color: `).concat(e("toggleswitch.hover.border.color"),`;
}

.p-toggleswitch:not(.p-disabled):has(.p-toggleswitch-input:hover) .p-toggleswitch-handle {
    background: `).concat(e("toggleswitch.handle.hover.background"),`;
    color: `).concat(e("toggleswitch.handle.hover.color"),`;
}

.p-toggleswitch:not(.p-disabled):has(.p-toggleswitch-input:hover).p-toggleswitch-checked .p-toggleswitch-slider {
    background: `).concat(e("toggleswitch.checked.hover.background"),`;
    border-color: `).concat(e("toggleswitch.checked.hover.border.color"),`;
}

.p-toggleswitch:not(.p-disabled):has(.p-toggleswitch-input:hover).p-toggleswitch-checked .p-toggleswitch-handle {
    background: `).concat(e("toggleswitch.handle.checked.hover.background"),`;
    color: `).concat(e("toggleswitch.handle.checked.hover.color"),`;
}

.p-toggleswitch:not(.p-disabled):has(.p-toggleswitch-input:focus-visible) .p-toggleswitch-slider {
    box-shadow: `).concat(e("toggleswitch.focus.ring.shadow"),`;
    outline: `).concat(e("toggleswitch.focus.ring.width")," ").concat(e("toggleswitch.focus.ring.style")," ").concat(e("toggleswitch.focus.ring.color"),`;
    outline-offset: `).concat(e("toggleswitch.focus.ring.offset"),`;
}

.p-toggleswitch.p-invalid > .p-toggleswitch-slider {
    border-color: `).concat(e("toggleswitch.invalid.border.color"),`;
}

.p-toggleswitch.p-disabled {
    opacity: 1;
}

.p-toggleswitch.p-disabled .p-toggleswitch-slider {
    background: `).concat(e("toggleswitch.disabled.background"),`;
}

.p-toggleswitch.p-disabled .p-toggleswitch-handle {
    background: `).concat(e("toggleswitch.handle.disabled.background"),`;
}
`)},ee={root:{position:"relative"}},te={root:function(t){var e=t.instance,d=t.props;return["p-toggleswitch p-component",{"p-toggleswitch-checked":e.checked,"p-disabled":d.disabled,"p-invalid":e.$invalid}]},input:"p-toggleswitch-input",slider:"p-toggleswitch-slider",handle:"p-toggleswitch-handle"},ne=A.extend({name:"toggleswitch",theme:Z,classes:te,inlineStyles:ee}),le={name:"BaseToggleSwitch",extends:W,props:{trueValue:{type:null,default:!0},falseValue:{type:null,default:!1},readonly:{type:Boolean,default:!1},tabindex:{type:Number,default:null},inputId:{type:String,default:null},inputClass:{type:[String,Object],default:null},inputStyle:{type:Object,default:null},ariaLabelledby:{type:String,default:null},ariaLabel:{type:String,default:null}},style:ne,provide:function(){return{$pcToggleSwitch:this,$parentInstance:this}}},j={name:"ToggleSwitch",extends:le,inheritAttrs:!1,emits:["change","focus","blur"],methods:{getPTOptions:function(t){var e=t==="root"?this.ptmi:this.ptm;return e(t,{context:{checked:this.checked,disabled:this.disabled}})},onChange:function(t){if(!this.disabled&&!this.readonly){var e=this.checked?this.falseValue:this.trueValue;this.writeValue(e,t),this.$emit("change",t)}},onFocus:function(t){this.$emit("focus",t)},onBlur:function(t){var e,d;this.$emit("blur",t),(e=(d=this.formField).onBlur)===null||e===void 0||e.call(d,t)}},computed:{checked:function(){return this.d_value===this.trueValue}}},se=["data-p-checked","data-p-disabled"],oe=["id","checked","tabindex","disabled","readonly","aria-checked","aria-labelledby","aria-label","aria-invalid"];function ie(l,t,e,d,u,s){return b(),w("div",x({class:l.cx("root"),style:l.sx("root")},s.getPTOptions("root"),{"data-p-checked":s.checked,"data-p-disabled":l.disabled}),[n("input",x({id:l.inputId,type:"checkbox",role:"switch",class:[l.cx("input"),l.inputClass],style:l.inputStyle,checked:s.checked,tabindex:l.tabindex,disabled:l.disabled,readonly:l.readonly,"aria-checked":s.checked,"aria-labelledby":l.ariaLabelledby,"aria-label":l.ariaLabel,"aria-invalid":l.invalid||void 0,onFocus:t[0]||(t[0]=function(){return s.onFocus&&s.onFocus.apply(s,arguments)}),onBlur:t[1]||(t[1]=function(){return s.onBlur&&s.onBlur.apply(s,arguments)}),onChange:t[2]||(t[2]=function(){return s.onChange&&s.onChange.apply(s,arguments)})},s.getPTOptions("input")),null,16,oe),n("div",x({class:l.cx("slider")},s.getPTOptions("slider")),[n("div",x({class:l.cx("handle")},s.getPTOptions("handle")),[H(l.$slots,"handle",{checked:s.checked})],16)],16)],16,se)}j.render=ie;const ae={class:"w-[200px] text-sm shrink-0 mx-2 [&>div]:text-xs"},re={class:"inline-block pl-2 truncate"},ce={class:"my-2"},de=["for"],ue={class:"truncate"},ge={class:"my-2"},he=["for"],pe={class:"truncate"},me={__name:"index.client",setup(l){const t=[{id:"0",name:"Великий зал"},{id:"1",name:"Малий зал"}],e=[{id:"1",name:"Аліна Кучерява",avatar:"dump/alina-k.png",email:"alina@gmail.com"},{id:"2",name:"Аліна Руда",avatar:"dump/alina-r.png",email:"ruda@gmail.com"},{id:"3",name:"Наталі",avatar:"dump/natali.png",email:"natali@gmail.com"},{id:"4",name:"Тетяна",avatar:"dump/tetiana.png",email:"tetiana@gmail.com"}],d=[{id:"1",name:"Полденс",color:"#CFFBFD"},{id:"2",name:"Полденс (+діти)",color:"#FFFAD3"},{id:"3",name:"Розтяжка",color:"#E5E8FF"},{id:"4",name:"Екзот",color:"#FFEBFF"},{id:"5",name:"Хілзи",color:"#FFD6D6"},{id:"6",name:"Тверк",color:"#FFD9C7"},{id:"7",name:"Функціонал",color:"#C9FFD4"}],u=f(!1),s=f(["2"]),r=f(["3"]);return(o,a)=>{const i=j,g=q,p=I;return b(),w("aside",ae,[a[4]||(a[4]=n("div",{class:"mb-3 text-neutral-500 mt-6"},"ПРИМІЩЕННЯ",-1)),n("ul",null,[(b(),w(y,null,F(t,c=>n("li",{key:c.id,class:"flex items-center mb-4"},[a[3]||(a[3]=n("i",{class:"inline-block pi pi-building text-neutral-400"},null,-1)),n("span",re,v(c.name),1),m(i,{modelValue:h(u),"onUpdate:modelValue":a[0]||(a[0]=k=>$(u)?u.value=k:null),class:"ml-auto"},null,8,["modelValue"])])),64))]),a[5]||(a[5]=n("div",{class:"text-neutral-500 mt-6"},"ТРЕНЕРИ",-1)),n("ul",ce,[(b(),w(y,null,F(e,c=>n("li",{key:c.id,class:"mb-2 flex items-center"},[m(g,{modelValue:h(s),"onUpdate:modelValue":a[1]||(a[1]=k=>$(s)?s.value=k:null),inputId:c.id+"-user-id",value:c.id},null,8,["modelValue","inputId","value"]),n("label",{for:c.id+"-user-id",class:"flex items-center cursor-pointer"},[m(p,{src:c.avatar,class:"mx-1.5 shrink-0",width:"25",alt:"user avatar"},null,8,["src"]),n("span",ue,v(c.name),1)],8,de)])),64))]),a[6]||(a[6]=n("div",{class:"text-neutral-500 mt-6"},"ПОСЛУГИ",-1)),n("ul",ge,[(b(),w(y,null,F(d,c=>n("li",{key:c.id,class:"mb-3 flex items-center"},[m(g,{modelValue:h(r),"onUpdate:modelValue":a[2]||(a[2]=k=>$(r)?r.value=k:null),inputId:c.id+"-service-id",value:c.id},null,8,["modelValue","inputId","value"]),n("div",{class:"w-5 h-5 mx-2 rounded",style:D({backgroundColor:c.color})},null,4),n("label",{for:c.id+"-service-id",class:"flex items-center cursor-pointer"},[n("span",pe,v(c.name),1)],8,he)])),64))])])}}},ve={class:"flex items-center"},be={class:"font-bold text-lg"},we={class:"grid grid-cols-2 gap-2"},fe={class:"mt-3"},ke={class:"mt-2"},ye={class:"flex items-center"},xe={class:"mt-6"},_e={class:"flex"},Fe={class:"font-bold ml-auto self-end text-sm"},$e={class:"mt-4"},Ce={class:"max-h-[200px] overflow-y-auto"},Ve={class:"mb-3"},Se={__name:"Modal",setup(l){const{event:t,setEvent:e}=U(),d=f(!1);O(()=>t.value,()=>{if(!t.value){d.value=!1;return}d.value=!0});const u=r=>{const o=new Date(r),a=o.getHours().toString().padStart(2,"0"),i=o.getMinutes().toString().padStart(2,"0");return`${a}:${i}`},s=f("");return(r,o)=>{const a=I,i=G,g=J,p=K;return b(),M(p,{position:"right",visible:h(d),"onUpdate:visible":o[1]||(o[1]=()=>{h(e)(void 0)}),modal:!0,dismissableMask:!0,class:"right-0 top-[3.46rem] w-[400px] min-h-[500px] bg-white border"},{header:B(()=>[n("div",ve,[n("div",{style:D({backgroundColor:h(t).event.backgroundColor}),class:"w-5 h-5 rounded-sm mr-3"},null,4),n("div",be,v(h(t).event.title),1)])]),default:B(()=>[n("section",null,[n("div",we,[n("div",fe,[o[2]||(o[2]=n("div",{class:"text-neutral-500 text-xs mb-1"},"Час тренування",-1)),n("div",null,v(u(h(t).event.start))+" - "+v(u(h(t).event.end)),1)]),o[4]||(o[4]=n("div",{class:"mt-3"},[n("div",{class:"text-neutral-500 text-xs mb-1"},"Тип заходу"),n("div",{class:"flex items-center"},"Групові")],-1)),o[5]||(o[5]=n("div",{class:"mt-2"},[n("div",{class:"text-neutral-500 text-xs mb-1"},"Приміщення"),n("i",{class:"inline-block pi pi-building text-neutral-400"}),n("div",{class:"inline-block relative ml-2"},"Великий Зал")],-1)),n("div",ke,[o[3]||(o[3]=n("div",{class:"text-neutral-500 text-xs mb-1"},"Тренер",-1)),n("div",ye,[m(a,{src:h(t).event.extendedProps.employee.avatar,class:"inline-block mr-1",alt:"Employee avatar",width:"20"},null,8,["src"]),_(" "+v(h(t).event.extendedProps.employee.name),1)])])])]),n("section",xe,[n("div",_e,[o[7]||(o[7]=n("h3",{class:"font-bold"},"Клієнти",-1)),o[8]||(o[8]=_("   ")),n("div",Fe,[_(v(h(t).event.extendedProps.event.reserved.length),1),o[6]||(o[6]=n("span",{class:"mx-1"},"/",-1)),_(v(h(t).event.extendedProps.event.capacity),1)])])]),n("section",$e,[n("div",null,[m(i,{id:"on_label",type:"text",size:"small",modelValue:h(s),"onUpdate:modelValue":o[0]||(o[0]=c=>$(s)?s.value=c:null),placeholder:"Знайти та додати клієнта",class:"w-full mb-4"},null,8,["modelValue"])]),n("ul",Ce,[(b(!0),w(y,null,F(h(t).event.extendedProps.event.reserved,c=>(b(),w("li",{key:c.id,class:"relative p-2 border rounded-md mb-2"},[n("div",Ve,v(c.name),1),m(g,{icon:"pi pi-times",severity:"secondary",rounded:"","aria-label":"Remove user",class:"!absolute !w-6 !h-6 top-2 right-2"},{default:B(()=>o[9]||(o[9]=[n("i",{class:"pi pi-times",style:{"font-size":"0.7rem"}},null,-1)])),_:1}),m(g,{class:"mr-2",size:"small",label:"Не прийшов(-ла)",outlined:""}),m(g,{size:"small",label:"списати заняття",outlined:""})]))),128))])])]),_:1},8,["visible"])}}},Be=X(me),Pe={__name:"index",setup(l){const{$setCalendar:t}=z(),e=f(void 0),d=f(null),{data:u,status:s}=Q("/api/events","$cWaDSM8gCO");O(s,()=>{r()}),T(()=>{e.value=t(d.value),r()});function r(){s.value!=="success"||!e.value||u.value.forEach(o=>{e.value.addEvent(o)})}return(o,a)=>{const i=Be,g=Se;return b(),w(y,null,[m(i),n("main",{ref_key:"ecRef",ref:d,class:"w-full overflow-y-scroll relative"},null,512),m(g)],64)}}},Te={};function Ee(l,t){const e=Pe;return b(),M(e)}const je=R(Te,[["render",Ee]]);export{je as default};
