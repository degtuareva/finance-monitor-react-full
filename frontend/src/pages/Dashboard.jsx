import {useEffect, useState} from 'react';
import {Bar} from 'react-chartjs-2';
import {BarElement, CategoryScale, Chart as ChartJS, Legend, LinearScale, Tooltip} from 'chart.js';
import {analytics} from '../api';

ChartJS.register(CategoryScale, LinearScale, BarElement, Legend, Tooltip);
export default function Dashboard() {
    const [m, setM] = useState(null);
    useEffect(() => {
        const d = new Date(), to = d.toISOString().slice(0, 10), from = `${d.getFullYear()}-01-01`;
        analytics.metrics(from, to).then(x => setM(x.data)).catch(() => setM({income: 0, expense: 0, balance: 0}));
    }, []);
    if (!m) return <p>Загрузка...</p>;
    return <><h1>Дашборд</h1>
        <section className="metrics">
            <div>Доходы: {m.income}</div>
            <div>Расходы: {m.expense}</div>
            <div>Баланс: {m.balance}</div>
        </section>
        <Bar data={{
            labels: ['Доходы', 'Расходы', 'Баланс'],
            datasets: [{label: 'Сумма', data: [m.income, m.expense, m.balance]}]
        }}/></>
}