import{u as k}from"./Dkxuxu-Q.js";import{s as x,a as $,b as S}from"./BKMwx7YX.js";import{c as C}from"./EsrfRR3E.js";import{B as _,o as s,c,z as A,y as v,t as d,I as h,N as B,O as E,P as L,J as F,a0 as N,a as o,b as i,w as l,C as f,E as D,F as M,A as P}from"./CgXyU8S8.js";import{u as T}from"./B0M2IW21.js";import"./OtOWOkM3.js";import"./BfvwIcQv.js";import"./bYOJpIkd.js";var V=function(t){var a=t.dt;return`
.p-avatar {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: `.concat(a("avatar.width"),`;
    height: `).concat(a("avatar.height"),`;
    font-size: `).concat(a("avatar.font.size"),`;
    background: `).concat(a("avatar.background"),`;
    color: `).concat(a("avatar.color"),`;
    border-radius: `).concat(a("avatar.border.radius"),`;
}

.p-avatar-image {
    background: transparent;
}

.p-avatar-circle {
    border-radius: 50%;
}

.p-avatar-circle img {
    border-radius: 50%;
}

.p-avatar-icon {
    font-size: `).concat(a("avatar.icon.size"),`;
    width: `).concat(a("avatar.icon.size"),`;
    height: `).concat(a("avatar.icon.size"),`;
}

.p-avatar img {
    width: 100%;
    height: 100%;
}

.p-avatar-lg {
    width: `).concat(a("avatar.lg.width"),`;
    height: `).concat(a("avatar.lg.width"),`;
    font-size: `).concat(a("avatar.lg.font.size"),`;
}

.p-avatar-lg .p-avatar-icon {
    font-size: `).concat(a("avatar.lg.icon.size"),`;
    width: `).concat(a("avatar.lg.icon.size"),`;
    height: `).concat(a("avatar.lg.icon.size"),`;
}

.p-avatar-xl {
    width: `).concat(a("avatar.xl.width"),`;
    height: `).concat(a("avatar.xl.width"),`;
    font-size: `).concat(a("avatar.xl.font.size"),`;
}

.p-avatar-xl .p-avatar-icon {
    font-size: `).concat(a("avatar.xl.icon.size"),`;
    width: `).concat(a("avatar.xl.icon.size"),`;
    height: `).concat(a("avatar.xl.icon.size"),`;
}

.p-avatar-group {
    display: flex;
    align-items: center;
}

.p-avatar-group .p-avatar + .p-avatar {
    margin-inline-start: `).concat(a("avatar.group.offset"),`;
}

.p-avatar-group .p-avatar {
    border: 2px solid `).concat(a("avatar.group.border.color"),`;
}

.p-avatar-group .p-avatar-lg + .p-avatar-lg {
    margin-inline-start: `).concat(a("avatar.lg.group.offset"),`;
}

.p-avatar-group .p-avatar-xl + .p-avatar-xl {
    margin-inline-start: `).concat(a("avatar.xl.group.offset"),`;
}
`)},j={root:function(t){var a=t.props;return["p-avatar p-component",{"p-avatar-image":a.image!=null,"p-avatar-circle":a.shape==="circle","p-avatar-lg":a.size==="large","p-avatar-xl":a.size==="xlarge"}]},label:"p-avatar-label",icon:"p-avatar-icon"},I=_.extend({name:"avatar",theme:V,classes:j}),O={name:"BaseAvatar",extends:F,props:{label:{type:String,default:null},icon:{type:String,default:null},image:{type:String,default:null},size:{type:String,default:"normal"},shape:{type:String,default:"square"},ariaLabelledby:{type:String,default:null},ariaLabel:{type:String,default:null}},style:I,provide:function(){return{$pcAvatar:this,$parentInstance:this}}},b={name:"Avatar",extends:O,inheritAttrs:!1,emits:["error"],methods:{onError:function(t){this.$emit("error",t)}}},q=["aria-labelledby","aria-label"],J=["src","alt"];function K(n,t,a,m,p,r){return s(),c("div",v({class:n.cx("root"),"aria-labelledby":n.ariaLabelledby,"aria-label":n.ariaLabel},n.ptmi("root")),[A(n.$slots,"default",{},function(){return[n.label?(s(),c("span",v({key:0,class:n.cx("label")},n.ptm("label")),d(n.label),17)):n.$slots.icon?(s(),h(E(n.$slots.icon),{key:1,class:B(n.cx("icon"))},null,8,["class"])):n.icon?(s(),c("span",v({key:2,class:[n.cx("icon"),n.icon]},n.ptm("icon")),null,16)):n.image?(s(),c("img",v({key:3,src:n.image,alt:n.ariaLabel,onError:t[0]||(t[0]=function(){return r.onError&&r.onError.apply(r,arguments)})},n.ptm("image")),null,16,J)):L("",!0)]})],16,q)}b.render=K;const X={class:"mx-4 my-6 w-full"},G={class:"mt-6"},H={class:"flex items-center"},ta=N({__name:"index",setup(n){const{data:t,status:a}=T("/api/services","$jAPXnSy9v6");return(m,p)=>{const r=$,y=b,z=S,g=C,w=x;return s(),c("main",X,[p[1]||(p[1]=o("h1",{class:"text-lg font-bold"},"Послуги",-1)),o("section",G,[i(w,{value:f(t),dataKey:"id",tableStyle:"min-width: 50rem"},{footer:l(()=>[i(g,{label:"Нова послуга",size:"small",severity:"secondary",variant:"text",icon:"pi pi-plus",onClick:()=>{}})]),default:l(()=>[i(r,{field:"name",header:"Назва"},{body:l(({data:e})=>[o("div",H,[o("div",{class:"w-5 h-5 mx-2 rounded",style:D({backgroundColor:e.color})},null,4),o("div",null,d(e.name),1)])]),_:1}),i(r,{field:"room",header:"Приміщення"},{body:l(({data:e})=>[p[0]||(p[0]=o("i",{class:"inline-block pi pi-building text-neutral-400 mr-2"},null,-1)),o("span",null,d(e.room||"Будь-яке"),1)]),_:1}),i(r,{field:"phone",header:"Співробітник"},{body:l(({data:e})=>[(s(!0),c(M,null,P(e.employees,u=>(s(),h(z,{key:u.id,value:u.name,class:"!text-black h-6 !font-normal !text-xs mr-1 !bg-neutral-50 border border-neutral-200 !pl-0",rounded:""},{default:l(()=>[i(y,{image:u.avatar,shape:"circle",class:"!w-auto !h-auto"},null,8,["image"]),o("div",null,d(u.name),1)]),_:2},1032,["value"]))),128))]),_:1}),i(r,{field:"duration",header:"Тривалість"},{body:l(({data:e})=>[o("div",null,d(("useFormatMinutes"in m?m.useFormatMinutes:f(k))(e.duration)),1)]),_:1}),i(r,null,{body:l(({data:e})=>[i(g,{icon:"pi pi-trash",severity:"danger",size:"small",variant:"text","aria-label":"Trash",class:"h-full",onOnClick:()=>{}})]),_:1})]),_:1},8,["value"])])])}}});export{ta as default};
