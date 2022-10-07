package ru.otus;

import ru.otus.exception.NotEnoughCashException;
import ru.otus.exception.TooMuchCashException;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Cash {

    private final Map<Denomination, BanknoteStack> banknoteMap;

    public static final String NOT_ENOUGH_MESSAGE = "В банкомате недостаточно банкнот для выдачи указанной суммы";
    public static final String TOO_MUCH_MESSAGE = "Заполнена ячейка с номиналом: ";

    private final int MAX_BANKNOTE_AMOUNT = 1000;

    public Cash(Map<Denomination, BanknoteStack> banknoteMap) {
        this.banknoteMap = banknoteMap;
    }

    public Cash(List<BanknoteStack> banknoteStackList) {
        this.banknoteMap = banknoteStackList.stream().collect(Collectors.toMap(BanknoteStack::getDenomination, Function.identity()));
    }

    public void sum(Cash in) {
        Map<Denomination, BanknoteStack> inMap = in.getMap();

        inMap.forEach((d, bs) -> {
            int newAmount = bs.getAmount() + banknoteMap.get(d).getAmount();

            if (newAmount > MAX_BANKNOTE_AMOUNT) {
                throw new TooMuchCashException(TOO_MUCH_MESSAGE + d.getValue());
            } else {
                banknoteMap.put(d, new BanknoteStack(d, newAmount));
            }
        });

    }

    public Cash subtract(int in) {
        isEnoughCash(in);
        isCorrectSum(in);

        return getCash(in);
    }

    private void isEnoughCash(int in) {
        if (in > this.balance())
            throw new NotEnoughCashException(NOT_ENOUGH_MESSAGE);
    }

    private void isCorrectSum(int in) {
        if (in % Denomination.HUNDRED.getValue() != 0)
            throw new NotEnoughCashException(NOT_ENOUGH_MESSAGE);
    }

    private Cash getCash(Integer in) {

        List<BanknoteStack> list = banknoteMap.values().stream()
                .sorted(Comparator.comparingInt(BanknoteStack::getValue).reversed()).toList();
        List<BanknoteStack> container = new ArrayList<>();

        int amount;
        for (BanknoteStack bs : list) {
            amount = in / bs.getValue();
            if (amount > 0) {
                amount = Math.min(amount, bs.getAmount());
                in = in - amount * bs.getValue();
                container.add(new BanknoteStack(bs.getDenomination(), amount));
                banknoteMap.put(bs.getDenomination(), new BanknoteStack(bs.getDenomination(), bs.getAmount() - amount));
            }
        }

        if (in > 0)
            throw new NotEnoughCashException(NOT_ENOUGH_MESSAGE);

        return new Cash(container);
    }

    public Map<Denomination, BanknoteStack> getMap() {
        return Collections.unmodifiableMap(this.banknoteMap);
    }

    public int balance() {
        return banknoteMap.values().stream().map(BanknoteStack::balance).reduce(Integer::sum).orElse(0);
    }

}
