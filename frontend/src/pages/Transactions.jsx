import {useEffect, useState} from 'react';
import {categories, transactions} from '../api';

export default function Transactions() {
    const [cats, setCats] = useState([]), [items, setItems] = useState([]), [f, setF] = useState({
        amount: '',
        type: 'EXPENSE',
        categoryId: '',
        transactionDate: new Date().toISOString().slice(0, 10),
        description: ''
    });
    const dates = () => {
        const d = new Date();
        return [`${d.getFullYear()}-01-01`, d.toISOString().slice(0, 10)]
    };
    const load = () => {
        let [a, b] = dates();
        transactions.list(a, b).then(r => setItems(r.data));
    };
    useEffect(() => {
        categories.list().then(r => {
            setCats(r.data);
            if (r.data[0]) setF(x => ({...x, categoryId: r.data[0].id}))
        });
        load()
    }, []);
    const submit = async e => {
        e.preventDefault();
        await transactions.create({...f, amount: Number(f.amount), categoryId: Number(f.categoryId)});
        load();
    };
    return <><h1>Транзакции</h1>
        <form onSubmit={submit}><input required type="number" min="0.01" step="0.01" placeholder="Сумма"
                                       value={f.amount} onChange={e => setF({...f, amount: e.target.value})}/><select
            value={f.type} onChange={e => setF({...f, type: e.target.value})}>
            <option value="EXPENSE">Расход</option>
            <option value="INCOME">Доход</option>
        </select><select value={f.categoryId} onChange={e => setF({
            ...f,
            categoryId: e.target.value
        })}>{cats.filter(c => c.type === f.type).map(c => <option key={c.id}
                                                                  value={c.id}>{c.name}</option>)}</select><input
            type="date" value={f.transactionDate} onChange={e => setF({...f, transactionDate: e.target.value})}/><input
            placeholder="Описание" value={f.description} onChange={e => setF({...f, description: e.target.value})}/>
            <button>Добавить</button>
        </form>
        <table>
            <thead>
            <tr>
                <th>Дата</th>
                <th>Тип</th>
                <th>Сумма</th>
                <th>Описание</th>
            </tr>
            </thead>
            <tbody>{items.map(x => <tr key={x.id}>
                <td>{x.transactionDate}</td>
                <td>{x.type}</td>
                <td>{x.amount}</td>
                <td>{x.description}</td>
            </tr>)}</tbody>
        </table>
    </>
}