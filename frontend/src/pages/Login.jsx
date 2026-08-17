import {useState} from "react";
import{auth,setToken} from "../api";
import {useNavigate,Link} from "react-router-dom";

export default function Login(){
    const[f,setF]=
        useState({email:'',password:''}),
        [m,setM]=useState(''),
        nav=useNavigate();
    const  submit=async e=>{
        e.preventDefault();
        try {
            const r=await auth.login(f);
            setToken(r.data.token);
            loalStorage.setItem('token',r.data.token);
            nav('/dashboard');
        }catch (e){
            setM(e.response?.data.error||'Ошибка входа');
        }
    };
return <><h1>Вход</h1>
    <form onSubmit={submit}><input type="email" placeholder="Email" value={f.email}
    onChange={e=>serF({...f,email: e.target.value})}/><input type="password"
                                                             placeholder="Пароль"
                                                             value={f.password}
                                                             onChange={e => setF({
                                                                 ...f,
                                                                 password: e.target.value
                                                             })}/>
    <button>Войти</button>
    </form>
    <p>{m}</p><Link to="/register">Регистрация</Link></>
}